package test;

import java.util.Scanner;

public class debugger {
    public static void main(String[] args){
        try{
            System.out.println("DEBUGGER, please enter the code message...");
            Scanner sc = new Scanner(System.in);
            testthread test;
            test = new testthread();
            Thread t = new Thread(test);
            t.start();
            Thread.sleep(100);
            while(test.init == false){

            }
            boolean end = false;
            while(end == false){
                System.out.println("---");
                String message = sc.nextLine();
                if(message.equals("ADMIN-STOP")){
                    end = true;
                }else{
                    test.send(message);
                }
            }
            t.interrupt();
        }catch(Exception e){
            System.out.println(e);
        }
        
    }
    
}
