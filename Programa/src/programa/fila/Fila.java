package fila;

import java.lang.reflect.Method;

/**
    * A classe Fila é uma classe de vetor que guarda dados de maneira sequencial, ou seja, de maneira first-in-first-out(FIFO).
    *
    * @author Caio Petrucci Rosa, Fábio Faúndes e Luís Sabença
*/
public class Fila <X> implements Cloneable {
	
	protected Object[] fila;
	protected int inicio;
	protected int fim;
	protected int qtd;
	protected double taxaCresc;
    
	/**
        * Método construtor que instancia o vetor da Fila e inicia suas variáveis, com 10 posições iniciais, e uma taxa de crescimento de 10%.
	*/
	public Fila() {
		this.iniciacao(10, 0.1);
	}

	/**
		* Método construtor que instancia o objeto com determinado número de posições no vetor, e uma taxa de crescimento de 10%.
		* @param tam É um valor inteiro que indicará o tamanho do vetor da fila.
		* @throws Exception O construtor lança uma exceção caso o tamanho passado como parâmetro seja inválido, ou seja, menor do que 1.
	*/
	public Fila(int tam) throws Exception {
		if (tam < 1)
            throw new Exception ("Tamanho de fila invalido");
        
        this.iniciacao(tam, 0.1);
	}

	/**
		* Método construtor que instancia o vetor a partir do valor passado por parâmetro, define a taxa de crescimento por parâmetro e inicia as variáveis da fila.
		* @param tam É um valor inteiro que indicará o tamanho do vetor da fila.
		* @param tc É um valor real que indicará a taxa de crescimento do vetor da fila.
		* @throws Exception O construtor lança uma exceção caso algum valor passado seja inválido.
	*/
	public Fila(int tam, double tc) throws Exception {
		if (tam < 1)
			throw new Exception ("Tamanho de fila invalido!");

		if (tc <= 0)
			throw new Exception ("Taxa de crescimento invalida!");
        
        this.iniciacao(tam, tc);
	}

	////////////////////////////

    protected void iniciacao(int tam, double tc) {
        this.fila = new Object[tam];
		this.inicio = 0;
		this.fim = -1;
		this.qtd = 0;
		this.taxaCresc = tc;
    }

	protected void cresca() {
		int novoTamanho = (int)Math.ceil((this.fila.length)*(1+this.taxaCresc));
		Object[] novaFila = new Object[novoTamanho];

		int i = this.inicio;
		int j = this.inicio;

		for (;;) {
			if (this.fila[i] instanceof Cloneable)
				novaFila[j] = this.cloneDe((X)this.fila[i]);
			else
				novaFila[j] = this.fila[i];

                        if (i == this.fim)
				break;
                        
                        if (i == this.fila.length-1)
				i = 0;
                        else
                            i++;
                        
			if (j >= novaFila.length)
				j = 0;
                        else
                            j++;
		}

		this.fila = novaFila;
	}

	protected X cloneDe(X x) {
		X ret = null;

		try {
			Class<?> classe = x.getClass();
			Class<?>[] tiposParametros = null;
			Method metodo = classe.getMethod("clone", tiposParametros);
			Object[] valoresParametros = null;
			ret = (X)metodo.invoke(x, valoresParametros);
		} catch(Exception e) {}
			
		return ret;
	}

	////////////////////////////

	/**
		* Método que inclui um novo elemento no final da fila, aumentando o tamanho do vetor, caso necessário.
		* @param novo É o novo elemento que será incluido no vetor
		* @throws Exception Método lança exceção caso o parâmetro passado seja nulo
	*/
	public void enfileirar(X novo) throws Exception {
		if (novo == null)
			throw new Exception("Elemento inexistente");

		if (this.qtd >= this.fila.length)
			this.cresca();

		if (this.fim >= this.fila.length-1)
			this.fim = 0;
		else 
			this.fim++;

		this.qtd++;

		if (novo instanceof Cloneable)
			this.fila[this.fim] = this.cloneDe(novo); 
		else
			this.fila[this.fim] = novo;
	}

	/**
		* Método que exclui o elemento localizado no início do vetor
		* @throws Exception Método lança exceção caso a fila já esteja vazia
	*/
	public void desenfileirar() throws Exception {
		if (this.qtd <= 0)
			throw new Exception("Pilha vazia");

		this.fila[this.inicio] = null;
		this.inicio++;
		this.qtd--;
	}

	/**
		* Método que retorna o elemento no início da fila
		* @return Retorna o elemento na primeira posição do vetor da fila
		* @throws Exception Método lança exceção caso a fila esteja vazia
	*/
	public X getElemento() throws Exception {
		if (this.qtd <= 0)
			throw new Exception("Pilha vazia");

		X elemento = null;

		if (this.fila[this.inicio] instanceof Cloneable)
			elemento = this.cloneDe((X)this.fila[this.inicio]);
		else 
			elemento = (X)this.fila[this.inicio]; 

		return elemento;
	}

	/**
		* Método que informa se a o vetor da fila está vazio ou não.
		* @return Retorna true ou false, indicando se a fila está vazia ou não.
	*/
	public boolean isVazia() {
		if (this.qtd <= 0)
			return true;

		return false;
	}

	////////////////////////

	/**
		* Método que retorna uma representação escrita do objeto
		* @return Retorna a representação escrita do objeto
	*/
	public String toString() {
		String str = "";

		str += this.inicio + ":";
		str += this.fim + ":";
		str += this.qtd + ":";
		str += this.taxaCresc + ":";
		
		str += "{";

                int i = this.inicio;
                for (;;) {
			str += this.fila[i] + ((i==this.fim)?"":":");

                        if (i == this.fim)
				break;
                        
                        if (i == this.fila.length-1)
				i = 0;
                        else
                            i++;
		}

		str += "}";

		return str;
	}

	/**
		* Método que retorna um código praticamente único do objeto, que é calculado a partir de seus atributos
		* @return Retorna um código praticamente único do objeto, que é calculado a partir de seus atributos
	*/
	public int hashCode() {
		int ret = 666;

		ret = ret*7 + new Integer(this.inicio).hashCode();
		ret = ret*7 + new Integer(this.fim).hashCode();
		ret = ret*7 + new Integer(this.qtd).hashCode();
		ret = ret*7 + new Double(this.taxaCresc).hashCode();

		int i = this.inicio;
                for (;;) {
			ret += ret*7 + this.fila[i].hashCode();

                        if (i == this.fim)
				break;
                        
                        if (i == this.fila.length-1)
				i = 0;
                        else
                            i++;
		}

		return ret;
	}

	/**
		* Método que compara um objeto qualquer com si mesmo, determinando se são iguais ou não
		* @param obj É o objeto que será usado para a comparação no método
		* @return Retorna o resultado da comparação, determinando se o objeto comparado é ou não igual
	*/
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (obj.getClass() != this.getClass())
			return false;

		Fila f = (Fila)obj;

		if (f.inicio != this.inicio)
			return false;

		if (f.fim != this.fim)
			return false;

		if (f.qtd != this.qtd)
			return false;

		if (f.taxaCresc != this.taxaCresc)
			return false;

		if (f.fila.length != this.fila.length)
			return false;

		if (!this.isVazia()) {
            int i = this.inicio;
            for (;;) {
                    if (!(this.fila[i].equals(f.fila[i])))
                            return false;

                    if (i == this.fim)
                            break;

                    if (i == this.fila.length-1)
                            i = 0;
                    else
                        i++;
            }
        }

		return true;
	}

	/**
		* Método contrutor que instancia o vetor da fila e atribui suas variáveis a partir de um objeto passado como modelo.
		* @param modelo É o modelo que será usado durante a construção da nova fila.
		* @throws Exception O método lança exceção caso a fila modelo passada seja nula.
	*/
	public Fila(Fila modelo) throws Exception {
		if (modelo == null)
			throw new Exception("Modelo inexistente");

		this.inicio = modelo.inicio;
		this.fim = modelo.fim;
		this.qtd = modelo.qtd;
		this.taxaCresc = modelo.taxaCresc;
		this.fila = new Object[modelo.fila.length];

		int i = this.inicio;
                for (;;) {
			if (modelo.fila[i] instanceof Cloneable)
				this.fila[i] = modelo.cloneDe((X)modelo.fila[i]);
			else
				this.fila[i] = modelo.fila[i]; 

                        if (i == this.fim)
				break;
                        
                        if (i == this.fila.length-1)
				i = 0;
                        else
                            i++;
		}
	}

	/**
		* Método que clona sua própria instância e retorna o clone
		* @return Retorna o clone do objeto, com as mesmas características e valores de variáveis
	*/
	public Object clone() {
		Fila ret = null;

		try {
			ret = new Fila(this);
		} catch(Exception e) {}

		return ret;
	}
} 