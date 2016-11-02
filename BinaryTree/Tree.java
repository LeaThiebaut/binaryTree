package BinaryTree;

public class Tree<AnyType> {

	private Node<AnyType> root = null; // Racine de l'arbre

	public void insert (AnyType elem) {
		Node<AnyType> newElem = new Node<AnyType>(elem);
		root = insert(root, newElem);
	}

	private Node<AnyType> insert (Node<AnyType> root, Node<AnyType> newelement) {
		//Création de l'arbre si 1er elements
		if (root == null)
			root = new Node <AnyType> (newelement.getVal());
		else 
		{
			//Arbre binaire --> Comparaison de INT 
			if (((int) newelement.getVal())>((int) root.getVal()))
			{
				//Ajout direct a droite si supérieur et pas de fils droit
				if (root.getRight() == null)
					root.setRight(newelement);
				//sinon descend au fils droit pour aller ajouter notre element a la suite
				else
					insert(root.getRight(),newelement);
			} 
			else if (((int) newelement.getVal())<((int) root.getVal()))
			{	
				if (root.getLeft() == null){
					root.setLeft(newelement);
				}
				else
					insert(root.getLeft(),newelement);
			}
		}
		return root;

	}

	public int getHauteur () {
		return this.getHauteur(root);
	}

	private int getHauteur(Node<AnyType> tree) {
		return 0;
		// A complÃ©ter 

	}

	public String printTreePreOrder() {
		return "{"+this.printPreOrder(root) + "}";

	}

	private String printPreOrder(Node<AnyType> root) {
		String results = "";
	    if (root != null) {
	    	results+=printPreOrder(root.getLeft());
	    	results+=root.toString();
		    results+=printPreOrder(root.getRight());
		    }
		return results;
		
	}

	private class Node<AnyType> {
		AnyType val; 
		Node<AnyType> right;
		Node<AnyType> left; 

		public Node (AnyType val) {
			this.val = val;
			right = null;
			left = null;
		}

		public AnyType getVal() {
			return this.val;
		}

		public Node<AnyType> getRight() {
			return this.right;
		}

		public Node<AnyType> getLeft() {
			return this.left;
		}

		public void setRight(Node<AnyType> n) {
			this.right = n;
		}

		public void setLeft(Node<AnyType> n) {
			this.left = n;
		}
	
		public String toString() {
			return "("+this.val+")";
		}

	}


}

