package servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FileServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        System.out.println(req.getServletPath());
        if (req.getServletPath().equals("/copy.do")) {
            copy("e:/user.sql", "d:/拉拉.sql", false);
        } else if (req.getServletPath().equals("/ASD.do")) {
            upload(req);
        } else if (req.getServletPath().equals("/move.do")) {
            copy("e:/拉拉.txt", "d:/拉拉.txt", true);
        } else if (req.getServletPath().equals("/md5.do")) {
            String md = md5(req.getParameter("md5"));
            System.out.println(md);
            req.setAttribute("md5", md);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else if (req.getServletPath().equals("/copyj.do")) {
            copyJ("d:/ad1.rar", "e:/拉拉.rar", false);
        } else if (req.getServletPath().equals("/copyji.do")) {
            copyJi("e:/拉拉.rar", "d:/拉拉.rar", false);
        }else if(req.getServletPath().equals("/download.do")){
            String path=req.getParameter("path");
            Boolean online= Boolean.valueOf(req.getParameter("online"));
//            "d:/拉拉.txt"
            downLoad(path,resp,online);
        }else if(req.getServletPath().equals("/copyf.do")){
            copyf("e:/新建文本文档 (2).rar","d:/ad1.rar",false);
        }else if(req.getServletPath().equals("/down.do")){
            long l= System.currentTimeMillis();
            download(resp,req.getParameter("path"),new File(req.getParameter("path")).getName(),"d:/");
            long l1= System.currentTimeMillis();
            System.out.println(l1-l);
        }else if(req.getServletPath().equals("/download1.do")){
            long l= System.currentTimeMillis();
            downLoad1(req.getParameter("path"),resp,new File(req.getParameter("path")).getName());
            long l1= System.currentTimeMillis();
            System.out.println(l1-l);
        }else if(req.getServletPath().equals("/download2.do")){
            long l= System.currentTimeMillis();
            threadDownLoad(Integer.valueOf(req.getParameter("number")),req.getParameter("path"),resp,new File(req.getParameter("path")).getName());
            long l1= System.currentTimeMillis();
            System.out.println(l1-l);
        }else if(req.getServletPath().equals("/ll1.do")){
            try {
                String sql="SELECT * FROM `menu`.`admin` LIMIT 0, 1000";
                ResultSet rs=BaseUtil.exQ(sql);
                while (rs.next()){

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String md5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] bytes = str.getBytes();
            byte[] digest = md5.digest(bytes);
            char[] chars = new char[]{'0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void threadDownLoad(Integer number,String path,HttpServletResponse resp,String fileName) throws IOException {
        DownThread.threadCount=number;
        URL url=new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5*1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        resp.setContentType("application/x-msdownload");
        resp.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO-8859-1"));
        if(200==conn.getResponseCode()){
            int length=conn.getContentLength();
            RandomAccessFile file=new RandomAccessFile("D:/全部.txt","rwd");
            file.write((length+"").getBytes());
            file.close();
            RandomAccessFile raf=new RandomAccessFile("D:/"+new File(path).getName(),"rwd");
            raf.setLength(length);
            raf.close();
            int blocksize=length/DownThread.threadCount;
            for (int i=1;i<=DownThread.threadCount;i++){
                int startIndex=(i-1)*blocksize;
                int endIndex=i*blocksize-1;
                if(i==DownThread.threadCount){
                    endIndex=length;
                }
                System.out.println("线程【"+i+"】开始下载："+startIndex+"---->"+endIndex);
                new DownThread(path,i,startIndex,endIndex).start();
                DownThread.activeThread++;
                System.out.println("当前活动的线程数："+DownThread.activeThread);
            }
        }else {
            System.out.println("服务器异常，下载失败");
        }

    }

    public void downLoad1(String path, HttpServletResponse resp,String fileName) throws IOException {
        URL url=new URL(path);
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5*1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream=conn.getInputStream();
//        File file = new File(path);
//        String fileName=file.getName();
//        String ext=fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase();
//        if (!file.exists()) {
//            resp.sendError(404, "File not found");
//        }
//        创建读取文件流
//        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream out=null;
//        防止br.available()获取值为空
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] buf = new byte[count];
        int len = 0;
//        清空头文件
//        resp.reset();
        resp.setContentType("application/x-msdownload");
        resp.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO-8859-1"));
        //创建写入文件流
        out=new BufferedOutputStream(resp.getOutputStream());
//        读取文件到buf
        while ((len = inputStream.read(buf)) != -1) {
                //写入文件
                out.write(buf,0,len);
        }

        if(out!=null) {
//        写入遗留数据,清空缓冲区
            out.flush();
//        关闭写入流
            out.close();
        }
        //        关闭读取流
        if(inputStream!=null){
            inputStream.close();
        }

    }
    public  void download(HttpServletResponse resp,String urlPath,String fileName,String savePath) throws IOException {
        URL url=new URL(urlPath);
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5*1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//        conn.setRequestProperty("Accept-Encoding", "identity");
        BufferedInputStream bis=new BufferedInputStream(conn.getInputStream());
        byte [] getData=readInput(bis);
        File saveDir=new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file=new File(saveDir+File.separator+fileName);
        resp.setContentType("application/x-msdownload");
        resp.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO-8859-1"));
        BufferedOutputStream bos=new BufferedOutputStream(resp.getOutputStream());
        bos.write(getData);
        if(bos!=null){
            bos.close();
        }
        if(bis!=null){
            bis.close();
        }
        System.out.println("info:"+url+" download success");
    }
    public  byte[] readInput(BufferedInputStream bis) throws IOException {
        int count=0;
        while (count==0){
            count=bis.available();
        }
        byte [] buffer=new byte[count];
        int len=0;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        while ((len=bis.read(buffer))!=-1){
            bos.write(buffer,0,len);
        }
        bos.close();
        return bos.toByteArray();
    }
    public void downLoad(String path, HttpServletResponse resp, boolean isOnline) throws IOException {
        File file = new File(path);
        String fileName=file.getName();
        String ext=fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase();
        if (!file.exists()) {
            resp.sendError(404, "File not found");
        }
//        创建读取文件流
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream out=null;
//        防止br.available()获取值为空
        int count = 0;
        while (count == 0) {
            count = br.available();
        }
        byte[] buf = new byte[count];
        int len = 0;
//        清空头文件
        resp.reset();
        if (isOnline) {
            URL url1 = new URL("file:///" + path);
            resp.setContentType(url1.openConnection().getContentType());
            resp.setHeader("Content-Disposition", "inline; filename=" + new String(fileName.getBytes(),"UTF-8"));
            resp.setCharacterEncoding("UTF-8");
        } else {
            resp.setContentType("application/x-msdownload");
            resp.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO-8859-1"));
            //创建写入文件流
             out=new BufferedOutputStream(resp.getOutputStream());
        }
        int length=0;
//        读取文件到buf
        while ((len = br.read(buf)) != -1) {
            if(isOnline){
                length+=len;
                resp.getWriter().println(len);
                resp.getWriter().println(length);
                resp.getWriter().println(new String(buf,0,len));
            }else {
                //写入文件
                out.write(buf,0,len);
            }
        }
//        关闭读取流
        br.close();
        if(out!=null) {
//        写入遗留数据,清空缓冲区
            out.flush();
//        关闭写入流
            out.close();
        }
    }

    public void copyJ(String oldPath, String newPath, boolean move) {
        try {
            FileInputStream fis = new FileInputStream(oldPath);
            FileOutputStream fos = new FileOutputStream(newPath);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                byte b1 = b[3];
                b[3] = b[4];
                b[4] = b1;
                fos.write(b);
            }
            fis.close();
            if (move) {
                new File(oldPath).delete();
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copyf(String oldPath, String newPath, boolean move) {
        try {
            FileInputStream fis = new FileInputStream(oldPath);
            FileOutputStream fos = new FileOutputStream(newPath);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                fos.write(b);
            }
            fis.close();
            if (move) {
                new File(oldPath).delete();
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyJi(String oldPath, String newPath, boolean move) {
        try {
            FileInputStream fis = new FileInputStream(oldPath);
            FileOutputStream fos = new FileOutputStream(newPath);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                byte b1 = b[4];
                b[4] = b[3];
                b[3] = b1;
                fos.write(b);
            }
            fis.close();
            if (move) {
                new File(oldPath).delete();
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copy(String oldPath, String newPath, boolean move) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(oldPath)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(newPath)));
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
            }
            br.close();
            if (move) {
                new File(oldPath).delete();
            }
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upload(HttpServletRequest req) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //文件名中文乱码处理也可以如此写
        upload.setHeaderEncoding("utf-8");
        //设置缓冲区大小与临时文件目录
        factory.setSizeThreshold(1024 * 1024 * 10);
        File uploadTemp = new File("e:\\uploadTemp");
        uploadTemp.mkdirs();
        factory.setRepository(uploadTemp);
        try {
            List<FileItem> list = upload.parseRequest(req);
            for (FileItem fileItem : list) {
                if (!fileItem.isFormField() && fileItem.getName() != null && !"".equals(fileItem.getName())) {
                    InputStream inputStream = fileItem.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    isr.close();
//                    String sql=fileItem.getString("UTF-8");
//                    System.out.println(sql);
                } else {
                    System.out.println(fileItem.getString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
