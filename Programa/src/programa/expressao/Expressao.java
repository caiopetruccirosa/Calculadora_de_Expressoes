package expressao;

import java.util.StringTokenizer;
import matriz.Matriz;
import fila.Fila;
import pilha.Pilha;

/*
    * A classe Expressao � uma classe de objetos que armazena uma express�o aritm�tica e possui m�todos para resolver e calcular
    * o resultado dessa express�o.
    *
    * @author Caio Petrucci Rosa, F�bio Fa�ndes e Lu�s Saben�a
*/
public class Expressao
{
    protected String exp;
    protected Pilha pilha;
    protected Fila fila;
    protected Matriz regras;
    protected StringTokenizer quebrador;
    protected final String operacoes = "+-*/^()";
    
    /**
        * M�todo que retorna uma representa��o escrita do objeto
        * @return Retorna a representa��o escrita do objeto
    */
    public String toString () 
    {
        String ret = "";
        Double aux = null;
        
        try
        {
            aux = new Double(this.solucao());
        }
        catch (Exception e)
        {}
        
        ret += "Express�o: " + this.exp + System.lineSeparator();
        ret += "Solu��o: " + aux + System.lineSeparator();
        
        return ret;
    }
    
    /**
        * M�todo que retorna um c�digo praticamente �nico do objeto, que � calculado a partir de seus atributos
        * @return Retorna um c�digo praticamente �nico do objeto, que � calculado a partir de seus atributos
    */
    public int hashCode ()
    {
        int ret = 24;
        
        ret += ret * 2 + this.exp.hashCode();
        ret += ret * 2 +this.operacoes.hashCode();
        ret += ret * 2 + this.regras.hashCode();
        ret += ret * 2 + this.pilha.hashCode();
        ret += ret * 2 + this.fila.hashCode();
        ret += ret * 2 + this.quebrador.hashCode();
        
        return ret;
    }
    
    /**
        * M�todo que compara um objeto qualquer com si mesmo, determinando se s�o iguais ou n�o
        * @param obj � o objeto que ser� usado para a compara��o no m�todo
        * @return Retorna o resultado da compara��o, determinando se o objeto comparado � ou n�o igual
    */
    public boolean equals (Object obj)
    {
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (!obj.getClass().equals(this.getClass()))
            return false;
        
        Expressao e = (Expressao) obj;
        
        if (!e.exp.equals(this.exp))
            return false;
        
        if (!e.pilha.equals(this.pilha))
            return false;
        
        if (!e.fila.equals(this.fila))
            return false;
        
        if (!e.regras.equals(this.regras))
            return false;
        
        if (!e.quebrador.equals(this.quebrador))
            return false;
        
        return true;
    }
    
    /**
        * M�todo que clona sua pr�pria inst�ncia e retorna o clone
        * @return Retorna o clone do objeto, com as mesmas caracter�sticas e valores de vari�veis
    */
    public Object clone ()
    {
        Expressao ret = null;
        
        try
        {
            ret = new Expressao(this);
        }
        catch (Exception e)
        {}
        
        return ret;
    }
    
    /**
        * M�todo contrutor que instancia um objeto da classe Expressao e atribui suas vari�veis a partir de um objeto passado como modelo.
        * @param e � o modelo que ser� usado durante a constru��o da nova pilha.
        * @throws Exception O m�todo lan�a exce��o caso a express�o modelo passada seja nula.
    */
    public Expressao (Expressao e) throws Exception
    {
        if (e == null)
            throw new Exception ("Modelo nulo!");
        
        this.exp        = e.exp;
        this.pilha      = (Pilha)e.pilha.clone();
        this.fila       = (Fila)e.fila.clone();
        this.quebrador  = e.quebrador;
        this.regras     = e.regras;
    }

    /*
        * O m�todo construtor armazena a express�o a ser resolvida, instancia uma pilha, uma pilha, um objeto da classe Matriz
        * e instancia um StringTokenizer para quebrar a express�o armazenada.
        * @param e O par�metro � uma String, devendo ser uma express�o aritm�tica.
        * @throws Exception O construtor lan�a exce��o caso a String passada ser� nula ou vazia.
    */
    public Expressao (String e) throws Exception
    {
        if (e == null || e.trim().equals(""))
            throw new Exception ("Expressao nula!");
        
        this.exp = e;
        //Pilha e fila tem que ser de String para poder guardar tanto opera��es quanto n�meros reais
        this.pilha = new Pilha<String>();
        this.fila = new Fila<String>();
        this.regras = new Matriz();
        this.quebrador = new StringTokenizer(this.exp, this.operacoes, true);
    }
    
    protected double operacao (double op1, double op2, char oper)
    {
        double resultado = 0;
        
        switch (oper)
        {
            case '+': resultado = op1 + op2; break;
            case '-': resultado = op1 - op2; break;
            case '*': resultado = op1 * op2; break;
            case '/': resultado = op1 / op2; break;
            case '^': resultado = Math.pow(op1, op2); break;
        }
        
        return resultado;
    }
    
    protected void notacaoPolonesa () throws Exception
    {
        //Fazer a primeira parte do programa que organiza a pilha e fila 
        while (this.quebrador.hasMoreTokens())
        {
            String aux = this.quebrador.nextToken();
            //Tenta dar parseDouble, se conseguir, bota na fila, se n�o, bota na pilha
            try
            {
                double nmr = (double)Double.parseDouble(aux);
                this.fila.enfileirar(aux);
            }
            catch (NumberFormatException e)
            {
                //Vai desempilhar at� a matriz indicar que deve parar
                while (!this.pilha.isVazia())
                {
                    if (this.regras.deveDesempilhar((String)this.pilha.getElemento(), aux))
                    {
                        //S� vai enfileirar se n�o for um par�nteses
                        String s = (String)this.pilha.getElemento();
                        if (!s.trim().equals("(") || s.trim().equals(")"))
                            this.fila.enfileirar(s.trim());
                        
                        this.pilha.desempilhar();
                    }
                    else
                    {
                        break; //deve sair pois a pilha j� est� pronta para receber o operador
                    }
                }
                
                //Quando n�o for mais para desempilhar, empilha
                if (!aux.equals(")"))
                    this.pilha.empilhar(aux);
            }
            catch (NullPointerException e) //N�o ir� acontecer
            {
                throw new Exception ("String nula!");
            }
        }
        
        //Quando acabar a express�o, a fila deve receber os operadores que estiverem na pilha
        while (!this.pilha.isVazia())
        {
            String s = (String)this.pilha.getElemento();
            if (s.trim().equals("("))
                throw new Exception ("Parenteses a sobrando!");
            
            this.fila.enfileirar(s.trim());
            this.pilha.desempilhar();
        }
            
        
    }
    
    protected void resolver () throws Exception
    {
        double op1 = 0, op2 = 0;
        char oper = ' ';
        
        //S� acaba quando a fila ficar vazia
        while (!this.fila.isVazia())
        {
            try
            {
                double nmr = (double)Double.parseDouble((String)this.fila.getElemento());
                this.pilha.empilhar(this.fila.getElemento());
                this.fila.desenfileirar();
            }
            catch (Exception e) //Se tiver erro, o elemento da fila � um operador e deve ser usado 
            {
                String s = (String)this.fila.getElemento();
                this.fila.desenfileirar();
                oper = s.charAt(0);
                
                //Se a pilha estiver vazia nesse ponto, h� um erro na expressao
                if (this.pilha.isVazia())
                    throw new Exception ("Sintaxe da expressao incorreta!");
                
                //Conseguir� dar parseFloat pois j� conseguiu antes
                //System.out.println(this.pilha.getElemento().getClass() + " " +this.pilha.getElemento());
                op2 = Double.parseDouble((String)this.pilha.getElemento());
                this.pilha.desempilhar();
                
                //Se a pilha estiver vazia nesse ponto, h� um erro na expressao
                if (this.pilha.isVazia())
                    throw new Exception ("Sintaxe da expressao incorreta!");
                
                op1 = Double.parseDouble((String)this.pilha.getElemento());
                this.pilha.desempilhar();
                
                //Empilha o resultado da expressao
                String resultado = "" + this.operacao(op1,op2,oper);
                this.pilha.empilhar(resultado);
            }
        }
    }
    
    /*
        * O m�todo solucao() resolve e calcula o resultado da express�o armanezada no objeto, retornando o valor final/resultado.
        * @throws O m�todo lan�a exce��o caso a express�o seja inv�lida ou exista alguma erro nela.
        * @return O m�todo retorna o valor resultante da express�o aritm�tica armazenada no objeto.
    */
    public double solucao () throws Exception
    {
        this.notacaoPolonesa();
        this.resolver();
        
        double ret = Double.parseDouble((String)this.pilha.getElemento());
        this.pilha.desempilhar();
        
        if (!this.pilha.isVazia())
            throw new Exception ("Sintaxe da expressao incorreta!");
        
        return ret;
    }
}