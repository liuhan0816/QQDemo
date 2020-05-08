package com.arthur.mychat.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @ClassName InOutStream
 * @Author liuhan
 * @Date 2020/1/10 15:44
 */
public class InOutStream {
    public static ByteArrayInputStream parse(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        if(out == null){
            baos = new ByteArrayOutputStream();
        }
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }
}
