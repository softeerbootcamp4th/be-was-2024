package webserver.http;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtils {

    public static int checkDif(String a, String b){
        for(int i=0; i<Math.min(a.length(), b.length()); i++){
            if(a.charAt(i)!=b.charAt(i)){
                System.out.println("dif:"+(int)a.charAt(i)+", "+(int)b.charAt(i));
                return i;
            }
        }
        System.out.println("no dif");
        return -1;
    }

    public static int checkDif(byte[] a, byte[] b){
        for(int i=0; i<Math.min(a.length, b.length); i++){
            if(a[i]!=b[i]){
                System.out.println("dif:"+(int)a[i]+", "+b[i]);
                return i;
            }
        }
        System.out.println("no dif");
        return -1;
    }

    public static void notImplementedYet(){
        fail("This test is not implemented yet.");
    }

}
