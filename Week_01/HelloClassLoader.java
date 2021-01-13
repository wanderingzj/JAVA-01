import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author zhongjie.wang
 * @date 2021/1/12
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> c = new HelloClassLoader().findClass("/Users/wanderingzj/Downloads/Hello/Hello.xlass");
            Object obj = c.getDeclaredConstructor().newInstance();
            Method method = c.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String path) throws ClassNotFoundException {
        File file = new File(path);
        String[] splits = path.split("/");
        String fileName = splits[splits.length - 1];
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        byte[] bytes = decode(file);
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return defineClass(className, bytes, 0, bytes.length);
    }

    private byte[] decode(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}