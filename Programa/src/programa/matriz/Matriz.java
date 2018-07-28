package matriz;

/*
    * A classe Matriz é uma classe de objetos que armazenam e determinam as regras para a formação de uma expressão em notação polonesa a partir
    * de uma expressão aritmética padrão.
    *
    * @author Caio Petrucci Rosa, Fábio Faúndes e Luís Sabença
*/
public class Matriz
{
    protected final boolean regras[][];
    protected final int linhas, colunas;
    protected final String operadores = "+-*/^()";
    
    /*
        * O método construtor atribui valores aos atributos da classe, preenchendo a matriz que define as regras
        * para transformar uma expressão aritmética em notação polonesa.
    */
    public Matriz ()
    {
        this.linhas = 7;
        this.colunas = 7;
        this.regras = new boolean[7][7];
        
        //São 7 colunas e 7 linhas diferentes, portanto 49 atribuições diferentes
            
        // "(" Na pilha
        this.regras[0][0] = false; 
        this.regras[0][1] = false;
        this.regras[0][2] = false;
        this.regras[0][3] = false;
        this.regras[0][4] = false;
        this.regras[0][5] = false;
        this.regras[0][6] = true;
        
        // "^" na pilha
        this.regras[1][0] = false;
        this.regras[1][1] = false;
        this.regras[1][2] = true;
        this.regras[1][3] = true;
        this.regras[1][4] = true;
        this.regras[1][5] = true;
        this.regras[1][6] = true;
            
        //"*" na pilha
        this.regras[2][0] = false;
        this.regras[2][1] = false;
        this.regras[2][2] = true;
        this.regras[2][3] = true;
        this.regras[2][4] = true;
        this.regras[2][5] = true;
        this.regras[2][6] = true;
        
        //"/" na pilha
        this.regras[3][0] = false; 
        this.regras[3][1] = false;
        this.regras[3][2] = true;
        this.regras[3][3] = true;
        this.regras[3][4] = true;
        this.regras[3][5] = true;
        this.regras[3][6] = true;
        
        //"+" na pilha
        this.regras[4][0] = false;
        this.regras[4][1] = false;
        this.regras[4][2] = false;
        this.regras[4][3] = false;
        this.regras[4][4] = true;
        this.regras[4][5] = true;
        this.regras[4][6] = true;
        
        //"-" na pilha
        this.regras[5][0] = false; 
        this.regras[5][1] = false;
        this.regras[5][2] = false;
        this.regras[5][3] = false;
        this.regras[5][4] = true;
        this.regras[5][5] = true;
        this.regras[5][6] = true;
        
        //")" na pilha
        this.regras[6][0] = false;
        this.regras[6][1] = false;
        this.regras[6][2] = false;
        this.regras[6][3] = false;
        this.regras[6][4] = false; 
        this.regras[6][5] = false;
        this.regras[6][6] = false;
    }
    
    /**
        * Método que retorna uma representação escrita do objeto
        * @return Retorna a representação escrita do objeto
    */
    public String toString ()
    {
        String ret = "{";

        ret += System.lineSeparator();
        for (int i = 0; i <= this.linhas -1; i++)
        {
            for (int j = 0; j <= this.colunas -1; j++)
                ret += this.regras[i][j] + ";";
            
            ret += System.lineSeparator(); //Pula uma linha para cada linha acabada;
        }
        
        ret += "}";
        return ret;
    }
    
    /**
        * Método que retorna um código praticamente único do objeto, que é calculado a partir de seus atributos
        * @return Retorna um código praticamente único do objeto, que é calculado a partir de seus atributos
    */
    public int hashCode()
    {
        int ret = 7;
        
        ret += ret * 3 + new Integer(this.linhas).hashCode();
        ret += ret * 3 + new Integer(this.colunas).hashCode();
        ret += ret * 3 + this.operadores.hashCode();
        
        for (int i = 0; i <= this.linhas - 1; i++)
            for (int j = 0; j <= this.colunas - 1; j++)
                ret += ret * 3 + new Boolean(this.regras[i][j]).hashCode();
        
        return ret;
    }
    
    /**
        * Método que compara um objeto qualquer com si mesmo, determinando se são iguais ou não
        * @param obj É o objeto que será usado para a comparação no método
        * @return Retorna o resultado da comparação, determinando se o objeto comparado é ou não igual
    */
    public boolean equals (Object obj)
    {
        if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Matriz))
			return false;

		Matriz m = (Matriz)obj;

		if (this.regras.length != m.regras.length)
			return false;

		for (int i  = 0; i < this.regras.length; i++)
			if (this.regras[i].length != m.regras[i].length)
				return false;

		for (int i = 0; i < this.regras.length; i++)
			for (int j = 0; j < this.regras[i].length; j++)
				if (this.regras[i][j] != m.regras[i][j])
					return false;

		return true;
    }
    
    protected int posicaoCorrespondente(char c)
    {
        int ret = -1;
        
        switch (c)
        {
            case '(': ret =  0; break;
            case '^': ret =  1; break;
            case '*': ret =  2; break;
            case '/': ret =  3; break;
            case '+': ret =  4; break;
            case '-': ret =  5; break;
            case ')': ret =  6; break;
        }
        
        return ret;
    }
    
     /**
        * Esse método indica se deve-se desempilhar ou não os operadores da pilha, de acordo com as regras determinadas na matriz armazenada no objeto.
        * @param naPilha O parâmetro passado é um operador que já armazenada em uma pilha.
        * @param novo O parâmetro passado é um operador novo, que ainda não está guardado pelo programa e está armazenada na expressão a ser resolvida.
        * @throws Exception O método lança exceção caso as Strings passadas como parâmetro sejam nulas ou vazias, ou caso
        * exista um caracter inválido na pilha de operadores da fila. 
        * @return O método retorna true ou false indicando se deve desempilhar da pilha de operadores, formando a notação polonesa.
     */
    public boolean deveDesempilhar (String naPilha, String novo) throws Exception
    {  
        if (naPilha == null || naPilha.trim().equals(""))
            throw new Exception ("Parametro da pilha vazio!");
        
        if (novo == null || novo.trim().equals(""))
            throw new Exception ("Parametro novo vazio!");
        
        CharSequence aux1 = naPilha;
        CharSequence aux2 = novo;
        
        if (!this.operadores.contains(aux1))
        {
            throw new Exception ("Caracter na Pilha invalido!" + aux1);
        }
            
        
        if (!this.operadores.contains(aux2))
            throw new Exception ("Caracter novo invalido!");
        
        int linha = this.posicaoCorrespondente (aux1.charAt(0));
        int coluna = this.posicaoCorrespondente (aux2.charAt(0));
        
        return this.regras[linha][coluna];
    }
}