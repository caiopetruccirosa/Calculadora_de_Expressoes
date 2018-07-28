import expressao.Expressao;
import java.io.*;

/**
    * <h1>Segundo Projeto de POO</h1> 
    * O Programa resolve e calcula o resultado de uma expressão aritmética com diversos operadores.
    *
    * @author Caio Petrucci Rosa, Fábio Faúndes e Luís Sabença
*/
public class Programa
{
    public static void main (String[] args)
    {
        Expressao exp = null;
        char resp = 'S';
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        
        while (resp == 'S')
        {
            System.out.println("Digite a expressao aritmetica a ser resolvida:");
            try
            {
                exp = new Expressao (teclado.readLine());
                System.out.println("Solucao :" + exp.solucao());                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                System.out.println("Erro: " + e.getMessage());
            }

            try
            {
                System.out.println("Gostaria de escrever outra expressao?(S/N)");
                resp = teclado.readLine().toUpperCase().trim().charAt(0);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }    
    }
}