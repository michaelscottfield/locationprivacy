package test;

import dummygenerator.Location;
import dummygenerator.LocationDummySet;
import dummygenerator.LocationDummySets;
import utils.Variable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Server{
    public int id;
    private int port;
    public double longitude;
    public double latitude;
    LocationDummySets locationdummysets = new LocationDummySets();
    public Server(int id, int port, double longitude, double latitude) {
        this.id = id;
        this.port = port;
        this.longitude = longitude;
        this.latitude = latitude;
        new ServerSocketThread();
    }

    /*public void send(int receiver_id, int destination_id, double lon, double lat){
        new SocketThread(receiver_id, destination_id, lon, lat);
    }*/
    public void send(int userid, Set<Integer> allids, Set<Integer> allusedids, int k, int lbsid, Vector<Double> reallon, Vector<Double> reallat){
        Kusers kusers = new Kusers();
        Set<Integer> locids = new HashSet<>();
        Set<Integer> kuserids;
        allusedids.add(lbsid);
        allusedids.add(userid);
        Location realloc = new Location(kusers.getlocid(reallon.get(0), reallat.get(0), locids),reallon.get(0), reallat.get(0));
        LocationDummySet firstdummies = locationdummysets.GetDummySet(realloc, k + 1);
        Location secondrealloc = new Location(kusers.getlocid(reallon.get(1), reallat.get(1), locids),reallon.get(1), reallat.get(1));
        Set<Integer> secondkuserids;
        secondkuserids = kusers.getkuserids(allusedids, k, allids);
        LocationDummySet seconddummies = locationdummysets.GetDummySet(secondrealloc, k + 1);
        int i = 0;
        longitude = secondrealloc.longitude;
        latitude = secondrealloc.latitude;
        long startTime=System.currentTimeMillis();
        for(int id:secondkuserids){
            Thread tempthread = new Thread(new SocketThread(id, lbsid, seconddummies.location_dummy_set.get(i).longitude, seconddummies.location_dummy_set.get(i).latitude));
            i ++;
            tempthread.start();
            try{
                tempthread.join();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("k="+k +"时，运行时间为:"+(endTime-startTime)+"ms");
    }

    public String send_information(int sender_id, int received_id, int destination_id, double longitude, double latitude){
        String result;
        result = "<" + String.valueOf(sender_id) + "," + String.valueOf(received_id) + "," +
                String.valueOf(destination_id) + "," + String.valueOf(longitude) + "," +
                String.valueOf(latitude);
        return result;
    }

    public class ServerSocketThread implements Runnable {
        private ServerSocket serverSocket = null;

        public ServerSocketThread() {
            try {
                this.serverSocket = new ServerSocket(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Thread(this).start();
        }

        public void run() {
            try {
                while(true){
                    Socket client = this.serverSocket.accept();
                    System.out.println("linked");
                    new ServerThread(client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class SocketThread implements Runnable {
        private int receiver_id;
        private int destination_id;
        private double lon;
        private double lat;
        private Socket socket = null;

        public SocketThread(int receiver_id, int destination_id, double lon, double lat) {
            this.receiver_id = receiver_id;
            this.destination_id = destination_id;
            this.lon = lon;
            this.lat = lat;
            //new Thread(this).start();
        }
        public void run() {
            try {
                socket = new Socket("0.0.0.0", this.receiver_id);
                socket.setSoTimeout(10000);
                //获取Socket的输出流，用来发送数据到服务端
                PrintStream out = new PrintStream(socket.getOutputStream());
                //获取Socket的输入流，用来接收从服务端发送过来的数据
                BufferedReader buf =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = send_information(id, this.receiver_id, this.destination_id, this.lon, this.lat);
                //发送数据到服务端
                out.println(str);
                try{
                    //从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
                    String echo = buf.readLine();
                    System.out.println(String.valueOf(id) + ": " + echo);
                }catch(SocketTimeoutException e){
                    e.printStackTrace();
                }
                if(socket != null){
                    //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
                    socket.close(); //只关闭socket，其关联的输入输出流也会被关闭
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class ServerThread implements Runnable {
        private Socket client = null;

        public ServerThread(Socket client){
            this.client = client;
            new Thread(this).start();
        }
        public void run() {
            try{
                //获取Socket的输出流，用来向客户端发送数据
                PrintStream out = new PrintStream(this.client.getOutputStream());
                //获取Socket的输入流，用来接收从客户端发送过来的数据
                BufferedReader buf = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
                boolean flag =true;
                while(flag){
                    //接收从客户端发送过来的数据
                    String str =  buf.readLine();
                    if(str == null || "".equals(str)){
                        flag = false;
                    }else{
                        String[] data_list = str.substring(1, str.length() - 1).split(",");
                        int sender_id = Integer.parseInt(data_list[0]);
                        int receiver_id = Integer.parseInt(data_list[1]);
                        int destination_id = Integer.parseInt(data_list[2]);
                        double lon = Double.valueOf(data_list[3]);
                        double lat = Double.valueOf(data_list[4]);
                        System.out.println(String.valueOf(id) + ": get message " + String.valueOf(lon) + ", " +
                                String.valueOf(lat) + " from " + String.valueOf(sender_id));
                        // 用户发来的请求
                        if(id == destination_id){
                            //服务商提供服务
                            System.out.println(String.valueOf(port) + "： provider received, this is the service");
                            out.println("provider received, this is the service");
                        } else {
                            Socket socket_middle = new Socket("0.0.0.0", destination_id);
                            socket_middle.setSoTimeout(10000);
                            //获取Socket的输出流，用来发送数据到服务端
                            PrintStream out_middle = new PrintStream(socket_middle.getOutputStream());
                            //获取Socket的输入流，用来接收从服务端发送过来的数据
                            BufferedReader buf_middle =  new BufferedReader(new InputStreamReader(socket_middle.getInputStream()));
                            String str_middle = send_information(id, destination_id, destination_id, lon, lat);
                            //发送数据到服务端
                            out_middle.println(str_middle);
                            try{
                                //从服务器端接收数据有个时间限制（系统自设，也可以自己设置），超过了这个时间，便会抛出该异常
                                String echo = buf_middle.readLine();
                                System.out.println(String.valueOf(port) + ": " + echo);
                                out.println(echo);
                            }catch(SocketTimeoutException e){
                                System.out.println("Time out, No response");
                            }
                            socket_middle.close(); //只关闭socket，其关联的输入输出流也会被关闭
                        }
                    }
                }
                out.close();
                client.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
