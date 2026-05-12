
/**
 * Write a description of class TestClass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.JOptionPane;
public class TestClass
{
   public static void main(String args[]){
       boolean bolRepeat = false;
       do{
       Interface I1 = new Interface();
       I1.run();
       //bolRepeat = Boolean.parseBoolean(JOptionPane.showInputDialog("Would you like to replay, true or false"));
    }while(bolRepeat == true);

   }
}
