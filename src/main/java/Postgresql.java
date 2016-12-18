import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by luosai on 2016/11/20.
 */
public class Postgresql {
    public static void main(String[] args) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://192.168.1.106:5432/postgres",
                            "Admin", "");
            //造数据
            double x=  0.002;
            double y = 0.002 ;
//            StringBuffer buffer = new StringBuffer("");
//            for (int i=50; i<100;i++){
//                for (int j =1000;j<10000;j++){
//                    buffer.append("INSERT INTO mylocation (geom,name,x,y) VALUES ( ")
//                            .append("  ST_GeomFromText('POINT(")
//                            .append(""+(x+i*0.001))
//                            .append(" ")
//                            .append(""+(y+j*0.001))
//                            .append(")', 4326),'zhangsan',")
//                            .append(""+(x+i*0.001)+","+(y+j*0.001)+");");
//                }
//            }
//           String sql = "INSERT INTO mylocation (geom,name,x,y) VALUES ( " +
//                    "  ST_GeomFromText('POINT(? ?)', 4326),'zhangsan',?,?  " +
//                    ");";
//            PreparedStatement preparedStatement = c.prepareStatement(buffer.toString());
//
//            boolean execute = preparedStatement.execute();
//            System.out.println(execute);






            String sql = "SELECT id, name,geom,x,y,   ST_DistanceSphere(  " +
                    "                      geom,  " +
                    "                      ST_GeometryFromText('POINT("+x+" "+ y+")')) distance  " +
                    "FROM mylocation  " +
                    "WHERE ST_DWithin(  " +
                    "  geom::geography,   " +
                    "  ST_GeomFromText('POINT("+x+""+ y+")', 4326)::geography,  " +
                    "  1000  " +
                    ") ORDER BY distance asc; " ;
            long pre = System.currentTimeMillis();
            PreparedStatement preparedStatement = c.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()){
                i ++ ;
                System.out.println(resultSet.getInt(1)+" "+resultSet.getString(2)+" "+resultSet.getDouble(4)+" "+resultSet.getDouble(5));

            }
            long now = System.currentTimeMillis();
            System.out.println("total size: " + i +"总耗时：" +(now-pre));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
