package servlet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public  class DownThread  extends Thread{
    private String path;
    private Integer threadId;
    private int startIndex;
    private int endIndex;
    //正处于连接数
    public static Integer activeThread=0;
    //最大连接数
    public static Integer threadCount;

    public DownThread(){}
    public DownThread(String path, Integer threadId, Integer startIndex, Integer endIndex) {
        this.path = path;
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        try {
            File tempFile=new File("D:/"+threadId+".txt");
            if(tempFile.exists()){
                FileInputStream fis=new FileInputStream(tempFile);
                byte [] buffer=new byte[1024];
                int len=fis.read(buffer);
                int downloadLength= Integer.parseInt(new String(buffer,0,len));
                startIndex=downloadLength;
                fis.close();
                System.out.println("线程【"+threadId+"】真实开始下载数据区间："+startIndex+"---->"+endIndex);
            }
            System.out.println(threadId+"进入");
            URL url=new URL(path);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
            int code=conn.getResponseCode();
            if(206==code){
                BufferedInputStream bis=new BufferedInputStream(conn.getInputStream());
                RandomAccessFile raf=new RandomAccessFile("D:/"+new File(path).getName(),"rwd");
                raf.seek(startIndex);
                int len=0;
                int count=0;
                int total=0;
                while (count==0){
                    count=bis.available();
                }
                byte [] buffer=new byte[count];

                while ((len=bis.read(buffer))!=-1){
                    RandomAccessFile file=new RandomAccessFile("D:/"+threadId+".txt","rwd");
                    raf.write(buffer,0,len);
                    total+=len;
                    System.out.println("线程【"+threadId+"】已下载数据："+(total+startIndex));
                    int bai=(total+startIndex)/(endIndex/100);
//                    int bai=(total+startIndex)*100/endIndex;
                    file.write((total+startIndex+"  "+bai+"%").getBytes());
                    file.close();
                }
                int i = 100;
                int bai=((total + startIndex) * i )/ endIndex;

                bis.close();
                raf.close();
                System.out.println("线程【"+threadId+"】下载完毕");
                activeThread--;
            }else {
                System.out.println("下载文件失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(activeThread==0){
                for (int i=1;i<=threadCount;i++){
                    File file=new File("D:/"+i+".txt");
                    file.delete();
                }
                File file=new File("D:/全部.txt");
                file.delete();
                System.out.println("下载完毕");
            }
        }
    }
}
