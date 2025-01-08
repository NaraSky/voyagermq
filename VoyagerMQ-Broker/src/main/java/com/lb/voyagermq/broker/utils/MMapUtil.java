package com.lb.voyagermq.broker.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MMapUtil {
    private File file; // 文件对象
    private MappedByteBuffer mappedByteBuffer; // 映射的内存缓冲区
    private FileChannel fileChannel; // 文件通道

    /**
     * 将指定的文件映射到内存中
     *
     * @param filePath    文件路径
     * @param startOffset 映射的起始偏移量
     * @param mappedSize  映射的大小（以字节为单位）
     * @throws IOException 如果文件不存在或映射失败
     */
    public void loadFileInMMap(String filePath, int startOffset, int mappedSize) throws IOException {
        // 创建文件对象
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        // 获取文件通道，用于与操作系统的映射文件交互
        fileChannel = new RandomAccessFile(file, "rw").getChannel();
        // 将文件从指定的偏移量开始映射到内存中
        mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 从文件映射内存的指定偏移量处读取内容
     *
     * @param readOffset 读取的起始偏移量
     * @param size       读取的字节数
     * @return 读取的字节数据
     */
    public byte[] readContent(int readOffset, int size) {
        mappedByteBuffer.position(readOffset); // 设置映射内存的读取位置
        byte[] content = new byte[size]; // 用于存储读取的数据
        int j = 0;
        for (int i = 0; i < size; i++) {
            // 从内存映射中读取字节数据
            byte b = mappedByteBuffer.get(readOffset + i);
            content[j++] = b;
        }
        return content;
    }

    /**
     * 高性能的写入数据到文件映射内存中
     *
     * @param content 要写入的字节数据
     */
    public void writeContent(byte[] content) {
        // 默认将数据写入映射内存
        this.writeContent(content, false);
    }

    /**
     * 将数据写入到映射内存中，并可选择是否强制同步到磁盘
     *
     * @param content 要写入的字节数据
     * @param force   是否强制同步到磁盘，true表示强制同步
     */
    public void writeContent(byte[] content, boolean force) {
        // 将数据写入映射内存
        mappedByteBuffer.put(content);
        if (force) {
            // 如果要求强制刷盘，将数据同步到磁盘
            mappedByteBuffer.force();
        }
    }

    /**
     * 清理映射内存并释放相关资源
     * 该方法通过反射调用清理内存的方法，确保释放内存资源
     */
    public void clean() {
        // 判断映射内存是否有效
        if (mappedByteBuffer == null || !mappedByteBuffer.isDirect() || mappedByteBuffer.capacity() == 0)
            return;
        // 使用反射调用清理器（Cleaner）来释放映射内存
        invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
    }

    /**
     * 使用反射调用指定对象的方法
     *
     * @param target      目标对象
     * @param methodName  方法名
     * @param args        方法参数类型
     * @return 方法调用结果
     */
    private Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    Method method = method(target, methodName, args); // 获取方法
                    method.setAccessible(true); // 设置方法可访问
                    return method.invoke(target); // 调用方法
                } catch (Exception e) {
                    throw new IllegalStateException(e); // 捕获异常并抛出运行时异常
                }
            }
        });
    }

    /**
     * 获取指定对象的指定方法
     *
     * @param target     目标对象
     * @param methodName 方法名
     * @param args       方法参数类型
     * @return 获取到的方法
     * @throws NoSuchMethodException 如果找不到方法
     */
    private Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args); // 获取公共方法
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args); // 获取私有方法
        }
    }

    /**
     * 获取字节缓冲区的视图
     *
     * @param buffer 输入的字节缓冲区
     * @return 视图缓冲区
     */
    private ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer"; // 默认方法名称
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment"; // 如果有attachment方法，则使用该方法
                break;
            }
        }

        // 获取字节缓冲区的视图
        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer; // 如果没有视图，返回原缓冲区
        else
            return viewed(viewedBuffer); // 递归获取视图
    }

    public static void main(String[] args) throws IOException, InterruptedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Scanner input = new Scanner(System.in); // 创建输入扫描器
        int i = 0;
        int size = input.nextInt(); // 获取用户输入的映射大小（以MB为单位）
        MMapUtil mMapUtil = new MMapUtil(); // 创建MMapUtil实例
        // 默认将文件映射到内存，大小为用户指定的值
        mMapUtil.loadFileInMMap("/home/lb/Downloads/VoyagerMQ/broker/store/order_cancel_topic/00000000", 0, 1024 * 1024 * size);
        System.out.println("映射了" + size + "MB的空间");
        TimeUnit.SECONDS.sleep(5); // 暂停5秒
        System.out.println("释放内存");
        mMapUtil.clean(); // 清理映射内存
        TimeUnit.SECONDS.sleep(10000); // 程序等待10秒，确保内存已释放
    }
}
