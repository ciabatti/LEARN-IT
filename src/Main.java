import Orm.ConnectionManager;

public class Main {
    public static void main(String[] args) {
        try {
            var k= ConnectionManager.getConnection();
            System.out.println("Connection established successfully!");

        }catch (Exception e){
            System.out.println("ECCEZIONE: " + e.getMessage());
        }



    }
}