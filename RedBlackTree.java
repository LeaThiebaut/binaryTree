package RougeNoir;
import java.util.LinkedList;
import java.util.Queue;


public class RedBlackTree<T extends Comparable<? super T> > 
{
	private RBNode<T> root;  // Racine de l'arbre

	enum ChildType{ left, right }

	public RedBlackTree()
	{ 
		root = null;
	}

	public void printFancyTree()
	{
		printFancyTree( root, "", ChildType.right);
	}

	private void printFancyTree( RBNode<T> t, String prefix, ChildType myChildType)
	{
		System.out.print( prefix + "|__"); // un | et trois _

		if( t != null )
		{
			boolean isLeaf = (t.isNil()) || ( t.leftChild.isNil() && t.rightChild.isNil() );

			System.out.println( t );
			String _prefix = prefix;

			if( myChildType == ChildType.left )
				_prefix += "|   "; // un | et trois espaces
				else
					_prefix += "   " ; // trois espaces

			if( !isLeaf )
			{
				printFancyTree( t.leftChild, _prefix, ChildType.left );
				printFancyTree( t.rightChild, _prefix, ChildType.right );
			}
		}
		else
			System.out.print("null\n");
	}

	public T find(int key)
	{
		return find(root, key);
	}

	private T find(RBNode<T> current, int key)
	{	
		//System.out.print(current.value+"\n");
	    
		if (current.value == null)
	        return null;
	    
		else if ((int)current.value==key)
	        return current.value;
	    
		else if (((int)current.value<key)&&(current.rightChild!=null))
	        return find(current.rightChild,key);
	    
		else if (((int)current.value>key)&&(current.leftChild!=null))
	        return find(current.leftChild,key);
	    
		else 
	    	return null;
	}

	public int getHauteur() {
		// TODO Auto-generated method stub
		return getHauteur(this.root);
	}


	private int getHauteur(RBNode<T> tree) {
		if (tree == null) {
			return 0;
		}

		// À COMPLÉTER

	}

	public void insert(T val)
	{
		insertNode( new RBNode<T>( val ) );
	}

	private void insertNode( RBNode<T> newNode )
	{ 
		if (root == null)  // Si arbre vide
			root = newNode;
		else
		{
			RBNode<T> position = root; // On se place a la racine

			while( true ) // on insere le noeud (ABR standard)
			{
				int newKey = newNode.value.hashCode();
				int posKey = position.value.hashCode();

				if ( newKey < posKey ) 
				{
					if ( position.leftChild.isNil() ) 
					{
						position.leftChild = newNode;
						newNode.parent = position;
						break;
					} 
					else 
						position = position.leftChild;
				} 
				else if ( newKey > posKey ) 
				{
					if ( position.rightChild.isNil() )
					{
						position.rightChild = newNode;
						newNode.parent = position;
						break;
					}
					else 
						position = position.rightChild;
				}
				else // pas de doublons
				return;
			}
		}

		insertionCases( newNode );
	}

	private void insertionCases( RBNode<T> X )
	{
		insertionCase1( X );
	}

	private void insertionCase1 ( RBNode<T> X )
	{
		//Cas racine + sortie
		if(root==X)
		{
			root.setToBlack();
			return;
		}
		
		insertionCase2( X );
	}

	private void insertionCase2( RBNode<T> X )
	{
		//Cas aucun cgt + sortie
		if(X.parent.isBlack())
		{
			return;
		}
		insertionCase3( X );
	}

	private void insertionCase3( RBNode<T> X )
	{
		//Cas cgt de couleur + passage a la suite
		if(X.parent.isRed()&&X.uncle().isRed())
		{
			X.grandParent().setToRed();
			X.parent.setToBlack();
			X.uncle().setToBlack();
			insertionCase1(X.grandParent());
		}
		insertionCase4( X );
	}

	private void insertionCase4( RBNode<T> X )
	{
		//cas parent/uncle rouge/noir --> traitement cas 4 ou cas 5
		if(X.parent.isRed()&&X.uncle().isBlack())
		{
			if(X==X.parent.leftChild&&X.parent==X.grandParent().rightChild)
			{
				rotateLeft(X);
				/*
				//Stock du parent
				RBNode<T> tmp = X.parent;
				//grand parent prend le petit fils en tant que fils
				X.grandParent().rightChild=X;
				X.parent=X.grandParent();
				//Parent recup fils du fils et devient fils de l'ancien fils
				tmp.leftChild=X.rightChild;
				X.rightChild=tmp;
				*/
				//Verif cas 5 sur le new fils
				insertionCase5(X.rightChild);
			}
			else if(X==X.parent.rightChild&&X.parent==X.grandParent().leftChild)
			{
				rotateRight(X);
				/*
				RBNode<T> tmp = X.parent;
				X.grandParent().leftChild=X;
				X.parent=X.grandParent();
				tmp.rightChild=X.leftChild;
				X.leftChild=tmp;
				X.leftChild.parent=X;
				*/
				insertionCase5(X.leftChild);
			}
			//Si pas cas 4 mais rentre dans uncle/parent rouge/noir alors cas 5
			else
				insertionCase5(X);
		}
	}

	private void insertionCase5( RBNode<T> X )
	{
		if(X.parent.isRed()&&X.uncle().isBlack())
		{
			if(X==X.parent.rightChild&&X.parent==X.grandParent().rightChild)
			{
				//Cgt Couleur
				X.grandParent().setToRed();
				X.parent.setToBlack();
				//Modification sur le parent du grand parent : son fils (gauche/droit selon ou il etait avant) devient le parent de notre noeud
				if(X.grandParent().parent.rightChild==X.grandParent())
					X.grandParent().parent.rightChild= X.parent;
				else
					X.grandParent().parent.leftChild=X.parent;
				//Le grand parent recupere le fils du parent
				X.grandParent().rightChild=X.parent.leftChild;
				//Le parent devient grand parent 
				X.grandParent().parent=X.parent;
				X.parent.leftChild=X.grandParent();
				X.parent.parent=X.grandParent().parent;
			}
			else if(X==X.parent.leftChild&&X.parent==X.grandParent().leftChild)
			{

				X.grandParent().setToRed();
				X.parent.setToBlack();

				if(X.grandParent().parent.rightChild==X.grandParent())
					X.grandParent().parent.rightChild= X.parent;
				else
					X.grandParent().parent.leftChild=X.parent;
				
				X.grandParent().leftChild=X.parent.rightChild;
				X.grandParent().parent=X.parent;
				X.parent.rightChild=X.grandParent();
				X.parent.parent=X.grandParent().parent;
			}
		}
		return; 
	}

	private void rotateLeft( RBNode<T> G )
	{
		RBNode<T> tmp = G.parent;
		//grand parent prend le petit fils en tant que fils
		G.grandParent().rightChild=G;
		G.parent=G.grandParent();
		//Parent recup fils du fils et devient fils de l'ancien fils
		tmp.leftChild=G.rightChild;
		G.rightChild=tmp;
		return; 
	}

	private void rotateRight( RBNode<T> G )
	{
		RBNode<T> tmp = G.parent;
		G.grandParent().leftChild=G;
		G.parent=G.grandParent();
		tmp.rightChild=G.leftChild;
		G.leftChild=tmp;
		G.leftChild.parent=G;
		return; 
	}
	public void printTreePreOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PreOrdre ( ");
			printTreePreOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreePreOrder( RBNode<T> P )
	{
		// A MODIFIER/COMPLÉTER
		return; 
	}

	public void printTreePostOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "PostOrdre ( ");
			printTreePostOrder( root );
			System.out.println( ")");
		}
		return;
	}

	private void printTreePostOrder( RBNode<T> P )
	{
		// A MODIFIER/COMPLÉTER
		return; 
	}


	public void printTreeAscendingOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "AscendingOrdre ( ");
			printTreeAscendingOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreeAscendingOrder( RBNode<T> P )
	{
		// A COMPLÉTER

	}


	public void printTreeDescendingOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "DescendingOrdre ( ");
			printTreeDescendingOrder( root );
			System.out.println( " )");
		}
		return;
	}

	private void printTreeDescendingOrder( RBNode<T> P )
	{
		// A COMPLÉTER

	}

	public void printTreeLevelOrder()
	{
		if(root == null)
			System.out.println( "Empty tree" );
		else
		{
			System.out.print( "LevelOrdre ( ");

			Queue<RBNode<T>> q = new LinkedList<RBNode<T>>();

			q.add(root);

			//  À COMPLÉTER

			System.out.println( " )");
		}
		return;
	}

	private static class RBNode<T extends Comparable<? super T> > 
	{
		enum RB_COLOR { BLACK, RED }  // Couleur possible

		RBNode<T>  parent;      // Noeud parent
		RBNode<T>  leftChild;   // Feuille gauche
		RBNode<T>  rightChild;  // Feuille droite
		RB_COLOR   color;       // Couleur du noeud
		T          value;       // Valeur du noeud

		// Constructeur (NIL)   
		RBNode() { setToBlack(); }

		// Constructeur (feuille)   
		RBNode(T val)
		{
			setToRed();
			value = val;
			leftChild = new RBNode<T>();
			leftChild.parent = this;
			rightChild = new RBNode<T>();
			rightChild.parent = this;
		}

		RBNode<T> grandParent()
		{
			if(this.parent!=null)
				if(this.parent.parent!=null)
					return this.parent.parent;
			return null;
		}

		RBNode<T> uncle()
		{
			if(this.grandParent()!=null)
			{
				if(this.grandParent().leftChild!=null)
				{
					if(this.grandParent().leftChild.value!=this.parent.value)
					{
						return this.grandParent().leftChild;
					}
				}
				if(this.grandParent().rightChild!=null)
				{
					if(this.grandParent().rightChild.value!=this.parent.value)
					{
						return this.grandParent().rightChild;
					}
				}
			}
			return null;
		}

		RBNode<T> sibling()
		{
			if(this.parent!=null)
			{
				if(this.parent.leftChild!=null)
				{
					if(this.parent.leftChild.value!=this.value)
					{
						return this.parent.leftChild;
					}
				}
				if(this.parent.rightChild!=null)
				{
					if(this.parent.rightChild.value!=this.value)
					{
						return this.parent.rightChild;
					}
				}
			}
			return null;
		}

		public String toString()
		{
			return value + " (" + (color == RB_COLOR.BLACK ? "black" : "red") + ")"; 
		}

		boolean isBlack(){ return (color == RB_COLOR.BLACK); }
		boolean isRed(){ return (color == RB_COLOR.RED); }
		boolean isNil(){ return (leftChild == null) && (rightChild == null); }

		void setToBlack(){ color = RB_COLOR.BLACK; }
		void setToRed(){ color = RB_COLOR.RED; }
	}
}
