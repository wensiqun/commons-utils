package cn.wensiqun.commons.structure;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a single node of a red-black tree.  It is a
 * recursive structure.  Relationships between nodes are 
 * doubly linked, with parent and child references.  Many characteristics
 * of trees may be detected with static methods.
 *
 * @version $Id: RedBlackTree.java 22 2006-08-21 19:27:26Z bailey $
 * @author, 2002 duane a. bailey, evan s. sandhaus
 * @see structure.AVLTree
 * @see structure.BinaryTree
 * @see structure.BinarySearchTree
 */
public class RedBlackHashTree<T, K extends RedBlackHashTreeComparable<T>, V>
{
    /**
     * The left child of this node, or EMPTY
     */
	private RedBlackHashTree<T, K, V> left;

    /**
     * The right child of this node, or EMPTY
     */
    private RedBlackHashTree<T, K, V> right;

    /**
     * The parent of this node, or null
     */
    private RedBlackHashTree<T, K, V> parent;

    
    private T compareObj;
    
    /**
     * The value stored in this node
     */
    HashMap<K, V> valueMap;

    /**
     * The color of this node - red or black (not red)
     */
    private boolean isRed;

    /**
     * the unique empty node; used as children on leaf trees and
     * as empty search trees.
     */
    //public static final RedBlackHashTree EMPTY = new RedBlackHashTree<String>();

    /**
     * A one-time constructor, for constructing empty trees.
     * @post Private constructor that generates the EMPTY node
     * @return the EMPTY node; leaves have EMPTY as children
     */
    public RedBlackHashTree()
    {
        valueMap = null;
        parent = null;
        left = right = this;
        isRed = false;  // empty trees below the leaves should be black
    }

     /**
     * Constructs a red-black tree with no children, value of the node 
     * is provided by the user
     *
     * @param value A (possibly null) value to be referenced by node
     * @pre key is a non-null Comparable
     * @post constructs a single node red-black tree
     */
    public RedBlackHashTree(K key, V v)
    {
        //Assert.pre(v != null, "Red-black tree values must be non-null.");
        valueMap = new HashMap<K, V>();
        valueMap.put(key, v);
        compareObj = key.getComparableObject();
        parent = null;
        left = right = new RedBlackHashTree<T, K, V>();
        isRed = false;  // roots of tree should be colored black
    }

    /**
     * Determines if this tree is red.
     *
     * @return Whether or not this node is red
     * @post returns whether or not this node is red
     */
    protected boolean isRed()
    {
        return isRed;
    }

    /**
     * Determines if this tree is black.
     *
     * @return Whether or not this node is black
     * @post returns whether or not this node is black
     */
    protected boolean isBlack()
    {
        return !isRed;
    }

    /**
     * Set this node to be a red node
     *
     * @post sets this node red 
     */
    protected void setRed()
    {
        isRed = true;
    }

    /**
     * Set this node to be a red or black node, depending on value of 
     * <code>isRed</code>.
     * @post sets this node red or black, depending on boolean isRed
     */
    protected void setRed(boolean isRed)
    {
        this.isRed = isRed;
    }

    /**
     * Set this node to be black
     * @post sets this node black
     */
    protected void setBlack()
    {
        isRed = false;
    }

  
    /**
     * Returns value associated with this node
     *
     * @post Returns value associated with this node
     * @return The node's value
     */
    public HashMap<K, V> value()
    {
        return valueMap;
    }

    /**
     * Get left subtree of current node
     *
     * @post Returns reference to left subtree, or null
     * @return The left subtree of this node
     */
    protected RedBlackHashTree<T, K, V> left()
    {
        return left;
    }

    /**
     * Get right subtree of current node
     *
     * @post Returns reference to right subtree, or null
     * @return The right subtree of this node
     */
    protected RedBlackHashTree<T, K, V> right()
    {
        return right;
    }


    /**
     * Get reference to parent of this node
     *
     * @post Returns reference to parent node, or null
     * @return Reference to parent of this node
     */
    protected RedBlackHashTree<T, K, V> parent()
    {
        return parent;
    }

    /**
     * Update the parent of this node
     *
     * @post Re-parents this node to parent reference, or null
     * @param newParent A reference to the new parent of this node
     */
    protected void setParent(RedBlackHashTree<T, K, V> newParent)
    {
        parent = newParent;
    }

    /**
     * Update the left subtree of this node.  Parent of the left subtree
     * is updated consistently.  Existing subtree is detached
     *
     * @pre newLeft is a non-null RedBlackTree node, possibly EMPTY
     * @post does nothing to the EMPTY node;
     *       else makes newLeft a left child of this, 
     *       and this newLeft's parent
    */
    protected void setLeft(RedBlackHashTree<T, K, V> newLeft)
    {
        if (isEmpty()) 
        	return;
        
        if (left.parent() == this) 
        	left.setParent(null);
        
        left = newLeft;
        left.setParent(this);
    }

    /**
     * Update the right subtree of this node.  Parent of the right subtree
     * is updated consistently.  Existing subtree is detached
     *
     * @pre newRight is a non-null RedBlackTree node, possibly EMPTY
     * @post does nothing to the EMPTY node;
     *       else makes newRight a right child of this, 
     *       and this newRight's parent
    */
    protected void setRight(RedBlackHashTree<T, K, V> newRight)
    {
        if (isEmpty()) 
        	return;
        
        if (right.parent() == this) 
        	right.setParent(null);
        
        right = newRight;
        right.setParent(this);
    }

    /**
     * Determine if this node is a left child
     *
     * @post Returns true if this is a left child of parent
     * @return True iff this node is a left child of parent
     */
    public boolean isLeftChild(){
        if (parent() == null) 
        	return false;
        
        return this == parent().left();
    }

    /**
     * Determine if this node is a right child
     *
     * @post Returns true if this is a right child of parent
     * @return True iff this node is a right child of parent
     */
    public boolean isRightChild(){
        if (parent() == null) 
        	return false;
        
        return this == parent().right();
    }

    /**
     * Returns true if tree is empty.
     *
     * @post Returns true iff the tree rooted at node is empty
     * @return True iff tree is empty
     */
    public boolean isEmpty()
    {
        return valueMap == null || valueMap.isEmpty();
    }

    
    /**
     * Determine if this node is a root.
     *
     * @post Returns true if this is a root node
     * @return true iff this is a root node
     */
    protected boolean isRoot()
    {
        return parent == null;
    }

    /**
     * Returns reference to root of tree containing n
     *
     * @pre this node not EMPTY
     * @post Returns the root of the tree node n
     * @return Root of tree
     */
    protected RedBlackHashTree<T, K, V> root()
    {
        RedBlackHashTree<T, K, V> result = this;
        while (!result.isRoot()) {
            result = result.parent();
        }
        return result;
    }

    /**
     * Compute the depth of a node.  The depth is the path length
     * from node to root
     *
     * @post Returns the depth of a node in the tree
     * @return The path length to root of tree
     */
    public int depth(){
        if (parent() == null) 
        	return 0;
        return 1 + parent.depth();
    }

    /**
     * Method to perform a right rotation of tree about this node
     * Node must have a left child.  Relation between left child and node
     * are reversed
     *
     * @pre This node has a left subtree
     * @post Rotates local portion of tree so left child is root
     */
    private void rotateRight()
    {
        // all of this information must be grabbed before
        // any of the references are set.  Draw a diagram for help
        RedBlackHashTree<T, K, V> parent = parent();
        RedBlackHashTree<T, K, V> newRoot = left();
        
        // is the this node a child; if so, a right child?
        boolean wasChild = !isRoot();
        boolean wasLeftChild = isLeftChild();

        // hook in new root (sets newRoot's parent, as well)
        setLeft(newRoot.right());

        // puts pivot below it (sets this's parent, as well)
        newRoot.setRight(this);

        if (wasChild) {
            if (wasLeftChild) parent.setLeft(newRoot);
            else              parent.setRight(newRoot);
        } 
        //else Assert.post(newRoot.isRoot(),"Rotate at root preserves root.");
    }

    /**
     * Method to perform a left rotation of tree about this node
     * Node must have a right child.  Relation between right child and node
     * are reversed
     *
     * @pre This node has a right subtree
     * @post Rotates local portion of tree so right child is root
     */
    private void rotateLeft()
    {
        // all of this information must be grabbed before
        // any of the references are set.  Draw a diagram for help
        RedBlackHashTree<T, K, V> parent = parent();  // could be null
        RedBlackHashTree<T, K, V> newRoot = right();
        // is the this node a child; if so, a left child?
        boolean wasChild = !isRoot();
        boolean wasRightChild = isRightChild();

        // hook in new root (sets newRoot's parent, as well)
        setRight(newRoot.left());

        // put pivot below it (sets this's parent, as well)
        newRoot.setLeft(this);

        if (wasChild) {
            if (wasRightChild) parent.setRight(newRoot);
            else               parent.setLeft(newRoot);
        } 
        //else Assert.post(newRoot.isRoot(),"Left rotate at root preserves root.");
    }

    /**
     * Add a value to the red black tree, performing neccisary rotations
     * and adjustments. 
     *
     * @param c The value to be added to the tree.
     * @return The new value of the root.
     * @pre c is a non-null Comparable value
     * @post adds a comparable value to the red-black tree;
     *       returns the modified tree
     */
    public OperatorResult<T, K, V> put(K k, V v)
    {
    	OperatorResult<T, K, V> result = insert(k, v);
        RedBlackHashTree<T, K, V> tree = result.getTree();  // first, do a plain insert
        tree.setRed();  // we insert nodes as red nodes - a first guess
        tree.redFixup();  // now, rebalance the tree
        result.tree = tree.root();
        return result;  // return new root
    }

    /**
     * Insert a (possibly duplicate) value to red-black search tree
     *
     * @param c The value to be inserted into the tree.
     * @pre c is a non-null Comparable value
     * @post c is inserted into search tree rooted at this
     */
    private OperatorResult<T, K, V> insert(K key, V val)
    {
        // trivial case - tree was empty:
        if (isEmpty()) 
        	return new OperatorResult<T, K, V>(new RedBlackHashTree<T, K, V>(key, val), 1);

        // decide to insert value to left or right of root:
        int compareRes = ((Comparable)key.getComparableObject()).compareTo(compareObj);
        if (compareRes < 0) {

            // if to left and no left child, we insert value as leaf 
            if (left().isEmpty()) {
                RedBlackHashTree<T, K, V> result = new RedBlackHashTree<T, K, V>(key, val);
                setLeft(result);
                return new OperatorResult<T, K, V>(result, 1);
            } else {
                // recursively insert to left
                return left().insert(key, val);
            }
        } else if (compareRes > 0) {

            // if to right and no left child, we insert value as leaf
            if (right().isEmpty()) {
                RedBlackHashTree<T, K, V> result = new RedBlackHashTree<T, K, V>(key, val);
                setRight(result);
                return new OperatorResult<T, K, V>(result, 1);
            } else {
                // recursively insert to right
                return right().insert(key, val);
            }
        } else {
        	int oldSize= value().size();
        	value().put(key, val);
        	return new OperatorResult<T, K, V>(this, value().size() - oldSize);
        }
    }

    /**
     * Takes a red node and, restores the red nodes of the tree  
     * to maintain red-black properties if this node has a red parent.
     *
     * @pre this node is a red node; if parent is red, violates property
     * @post red nodes of the tree are adjusted to maintain properties
     */
    void redFixup()
    {
        if (isRoot() || !parent().isRed()) {
            // ensure that root is black (might have been insertion pt)
            root().setBlack();
        } else {
            RedBlackHashTree<T, K, V> parent = parent();  // we know parent exists
            // since parent is red, it is not root; grandParent exists & black
            RedBlackHashTree<T, K, V> grandParent = parent.parent();
            RedBlackHashTree<T, K, V> aunt;  // sibling of parent (may exist)

            if (parent.isLeftChild())
            {
                aunt = grandParent.right();
                if (aunt.isRed()) {
                    // this:red, parent:red, grand:black, aunt:red
                    // push black down from gp to parent-aunt, but
                    // coloring gp red may introduce problems higher up
                    grandParent.setRed();
                    aunt.setBlack();
                    parent.setBlack();
                    grandParent.redFixup();
                } else {
                    if (isRightChild()) {
                        // this:red, parent:red, grand:black, aunt:black
                        // ensure that this is on outside for later rotate
                        parent.rotateLeft();
                        parent.redFixup(); // parent is now child of this
                    } else {
                        // assertion: this is on outside
                        // this:red, parent:red, gp: black, aunt:black
                        // rotate right @ gp, and make this & gp red sibs
                        // under black parent
                        grandParent.rotateRight();
                        grandParent.setRed();
                        parent.setBlack();
                    }
                }
            } else // parent.isRightChild()
            {
                aunt = grandParent.left();
                if (aunt.isRed()) {
                    // this:red, parent:red, grand:black, aunt:red
                    // push black down from gp to parent-aunt, but
                    // coloring gp red may introduce problems higher up
                    grandParent.setRed();
                    aunt.setBlack();
                    parent.setBlack();
                    grandParent.redFixup();
                } else {
                    if (isLeftChild()) {
                        // this:red, parent:red, grand:black, aunt:black
                        // ensure that this is on outside for later rotate
                        parent.rotateRight();
                        parent.redFixup(); // parent is now child of this
                    } else {
                        // assertion: this is on outside
                        // this:red, parent:red, gp: black, aunt:black
                        // rotate right @ gp, and make this & gp red sibs
                        // under black parent
                        grandParent.rotateLeft();
                        grandParent.setRed();
                        parent.setBlack();
                    }
                }
            }
        }
    }

    /**
     * Remove an value "equals to" the indicated value.  Only one value
     * is removed, and no guarantee is made concerning which of duplicate
     * values are removed.  Value returned is no longer part of the
     * structure
     * 
     * @param val Value sought to be removed from tree
     * @return Actual value removed from tree
     * @pre c is non-null
     * @post the value is removed; resulting tree is returned
     */
    public OperatorResult<T, K, V> remove(K key)
    {
        // find the target node - the node whose value is removed
        RedBlackHashTree<T, K, V> target = locate(key);
        
        if (target == null || target.isEmpty()) 
        	return new OperatorResult<T, K, V>(root(), null);

        V value;
        if(target.value().size() > 1) {
        	value = target.value().remove(key);
        	return new OperatorResult<T, K, V>(root(), value);
        } else {
        	value = target.value().get(key);
        }
        
        // determine the node to be disconnected:
        // two cases: if degree < 2 we remove target node;
        //            otherwise, remove predecessor
        RedBlackHashTree<T, K, V> freeNode;
        if (target.left().isEmpty() ||
            target.right().isEmpty()) // simply re-root tree at right
        {
            // < two children: target node is easily freed
            freeNode = target;
        } else {
            // two children: find predecessor
            freeNode = target.left();
            while (!freeNode.right().isEmpty())
            {
                freeNode = freeNode.right();
            }
            // freeNode is predecessor
        }

        target.valueMap = freeNode.valueMap; // move value reference
        target.compareObj = freeNode.compareObj;//move compareObj reference

        // child will be orphaned by the freeing of freeNode;
        // reparent this child carefully (it may be EMPTY)
        RedBlackHashTree<T, K, V> child;
        if (freeNode.left().isEmpty())
        {
            child = freeNode.right();
        } else {
            child = freeNode.left();
        }

        // if child is empty, we need to set its parent, temporarily
        child.setParent(freeNode.parent());
        if (!freeNode.isRoot())
        {
            if (freeNode.isLeftChild())
            {
                freeNode.parent().setLeft(child);
            } else {
                freeNode.parent().setRight(child);
            }
        }

        // Assertion: child has been reparented
        RedBlackHashTree<T, K, V> result = child.root();  
        
        if (freeNode.isBlack()) 
        	child.blackFixup();
        
        return new OperatorResult<T, K, V>(result.root(), value);
    }

    /**
     * If a black node has just been removed above this;
     * this node is the root of a black-height balanced tree, but
     * the ancestors of this node are shy one black node on this branch.
     * This method restores black-height balance to such an imbalanced
     * tree.
     *
     * @pre a black node has just been removed above this;
     *      this node is the root of a black-height balanced tree, but
     *      the ancestors of this node are shy one black node on this branch
     * @post the tree is black-height balanced
    */
    void blackFixup()
    {
        // if root - we're actually balanced; if red, set to black
        if (isRoot() || isRed())
        {
            setBlack();
        } else {
            RedBlackHashTree<T, K, V> sibling, parent; // temporary refs to relates
            // we hold onto our parent because the nodes shift about
            parent = parent();

            if (isLeftChild())
            {
                // our sibling: can't be a leaf (see text)
                sibling = parent.right();

                if (sibling.isRed()) // and, thus, parent is black
                {
                    // lower this, but leave black heights the same
                    // then reconsider node with a red parent
                    sibling.setBlack();
                    parent.setRed();
                    parent.rotateLeft();
                    blackFixup(); // this node might have adopted 
                } else
                if (sibling.left().isBlack() && sibling.right().isBlack())
                {
                    // sibling black with black children: sib can be red
                    // remove sib as one black node in sibling paths, and
                    // push missing black problem up to parent
                    sibling.setRed();
                    parent.blackFixup();
                } else {
                    if (sibling.right().isBlack())
                    {
                        // this:black, sib:black, sib.l:red, sib.r:black
                        // heighten sibling tree, making sib:r red and
                        // sib.l black (both sib.l's children were black)
                        sibling.left().setBlack();
                        sibling.setRed();
                        sibling.rotateRight();
                        sibling = parent.right();
                    }
                    // this: black, sib:black, sib:l black, sib.r:red 
                    // this tree deepens with parent as new black node
                    // sibling holds the previous parent color and
                    // sibling color (black) moves down to right;
                    // this adds a black node to all paths in this tree
                    // so we're done; finish by checking color of root
                    sibling.setRed(parent.isRed()); // copy color
                    parent.setBlack();
                    sibling.right().setBlack();
                    parent.rotateLeft();
                    root().blackFixup(); // finish by coloring root
                }
            } else { // isRightChild
                // our sibling: can't be a leaf (see text)
                sibling = parent.left();

                if (sibling.isRed()) // and, thus, parent is black
                {
                    // lower this, but leave black heights the same
                    // then reconsider node with a red parent
                    sibling.setBlack();
                    parent.setRed();
                    parent.rotateRight();
                    blackFixup(); // this node might have adopted 
                } else
                if (sibling.left().isBlack() && sibling.right().isBlack())
                {
                    // sibling black with black children: sib can be red
                    // remove sib as one black node in sibling paths, and
                    // push missing black problem up to parent
                    sibling.setRed();
                    parent.blackFixup();
                } else {
                    if (sibling.left().isBlack())
                    {
                        // this:black, sib:black, sib.r:red, sib.l:black
                        // heighten sibling tree, making sib:l red and
                        // sib.r black (both sib.r's children were black)
                        sibling.right().setBlack();
                        sibling.setRed();
                        sibling.rotateLeft();
                        sibling = parent.left();
                    }
                    // this: black, sib:black, sib:r black, sib.l:red 
                    // this tree deepens with parent as new black node
                    // sibling holds the previous parent color and
                    // sibling color (black) moves down to left;
                    // this adds a black node to all paths in this tree
                    // so we're done; finish by checking color of root
                    sibling.setRed(parent.isRed()); // copy color
                    parent.setBlack();
                    sibling.left().setBlack();
                    parent.rotateRight();
                    root().blackFixup(); // finish by coloring root
                }
            } 
        }
    }

    /**
     * Determines if the red black search tree contains a value
     *
     * @param val The value sought.  Should be non-null
     * @return True iff the tree contains a value "equals to" sought value
     *
     * @pre c is non-null
     * @post returns true iff c is contained within the tree
     */
    public boolean contains(K key)
    {
        return locate(key) != null;
    }
    
    /**
     * Locates a value in the search tree or returns the largest value
     * less than <code>value</code>.
     *
     * @pre c is non-null
     * @post returns a node of this tree that contains c, or null
     */
    private RedBlackHashTree<T, K, V> locate(K key)
    {
        if (isEmpty()) 
        	return null;
        int relation = ((Comparable)key.getComparableObject()).compareTo(compareObj);
        if (relation == 0) 
        	return this;
        if (relation < 0) 
        	return left().locate(key);
        else 
        	return right().locate(key);
    }
    
    protected RedBlackHashTree<T, K, V> locate(T compareObj) {
    	 if (isEmpty()) 
         	return null;
         int relation = ((Comparable)compareObj).compareTo(this.compareObj);
         if (relation == 0) 
         	return this;
         if (relation < 0) 
         	return left().locate(compareObj);
         else 
         	return right().locate(compareObj);
    } 

    /**
     * Returns a c-equivalent value from tree, or null.
     *
     * @param c The c-equivalent value we are looking for in the tree.
     * @pre c is non-null
     * @post returns a c-equivalent value from tree, or null
     */
    public V get(K key)
    {
        RedBlackHashTree<T, K, V> n = locate(key);
        if (n == null) 
        	return null;
        else 
        	return n.value().get(key);
    }
    
    /**
     * return a Map according the pass to compare object.
     * 
     * @param compareObj
     * @return
     */
    public Map<K, V> getMap(T compareObj) {
    	RedBlackHashTree<T, K, V> n = locate(compareObj);
        if (n == null) 
        	return null;
        else 
        	return n.value();
    }

    /**
     * Returns true if this node is consistently structured
     *
     * @post returns true if this node is consistently structured
     */
    public boolean consistency()
    {
        return/* wellConnected(null) &&*/ redConsistency() && blackConsistency();
    }

    /**
     * Returns the black height of this subtree.
     *
     * @pre tree is black-height balanced
     * @post returns the black height of this subtree
     */
    private int blackHeight()
    {
        if (isEmpty()) return 0;
        if (isBlack()) return 1 + left().blackHeight();
        else return  left().blackHeight();
    }

    /**
     * Returns true if no red node in subtree has red children
     * 
     * @post returns true if no red node in subtree has red children
     */
    private boolean redConsistency()
    {
        if (isEmpty()) return true;
        if (isRed() && (left().isRed() || right().isRed())) return false;
        return left().redConsistency() && right().redConsistency();
    }

    /**
     * Returns true if black properties of tree are correct
     *
     * @post returns true if black properties of tree are correct
     */
    private boolean blackConsistency()
    {
        if (!isRoot()) // must be called on root
        {
            //Assert.debug("Tree consistency not tested at root.");
            return false;
        }
        if (!isBlack()) // root must be black
        {
            //Assert.debug("Root is not black.");
            return false;
        }
        // the number of black nodes on way to any leaf must be same
        if (!consistentlyBlackHeight(blackHeight()))
        {
            //Assert.debug("Black height inconsistent.");
            return false;
        }
        return true;
    }

    /**
     * Checks to make sure that the black height of tree is height
     * @post checks to make sure that the black height of tree is height
     */
    private boolean consistentlyBlackHeight(int height)
    {
        if (isEmpty()) return height == 0;
        if (isBlack()) height--;
        return left().consistentlyBlackHeight(height) &&
            right().consistentlyBlackHeight(height);
    }

    /*
     * Returns true iff this tree is well-connected.
     */
    /*    public boolean wellConnected(RedBlackTree<E> expectedParent)
    {
        boolean ok = true;
        if (isEmpty())
        {
            if (left != this) {
                ok = false;
                Assert.debug("EMPTY left not self.");
            }
            if (right != this) {
                ok = false;
                Assert.debug("EMPTY right not self.");
            }
        } else {
            if (expectedParent != parent()) 
            {   
                E expectedParentValue;
                ok = false;
                if (expectedParent == null) expectedParentValue = "null";
                else expectedParentValue = expectedParent.value();
                E parentValue;
                if (parent == null) parentValue = "null";
                else parentValue = parent.value();
                Assert.debug("Parent of "+value()+" was not "+expectedParentValue+", but "+parentValue);
            }
            if (value == null) {
                ok = false;
                Assert.debug("null value found in tree");
            }
            ok = ok & left().wellConnected(this) &
                right().wellConnected(this);
        }
        return ok;
    }
    */
    
    public void print()
    {
        if (isEmpty()) return;
        left().print();
        System.out.println(value());
        right().print();
    }

    /**
     * Computes hash code associated with values of tree.
     *
     * @post computes hash code associated with values of tree
     */
    public int hashCode()
    {
        if (isEmpty()) return 0;
        int result = left().hashCode() + right().hashCode();
        if (value() != null) result += value().hashCode();
        return result;
    }

    /**
     * Support method for {@link #toString}. Returns R if this is node 
     * is a right child, L if this node is a left child and Root if this
     * node is the root.
     * 
     * @return R if this is node 
     * is a right child, L if this node is a left child and Root if this
     * node is the root.
     */
    private String getHand(){
        if (isRightChild()) return "R";
        if (isLeftChild()) return "L";
        return "Root";  
    }

    /**
     * Support method for {@link #toString}. Returns Red if this is node 
     * is a red, and Black if this node is black.
     * 
     * @return R if this is node 
     * is a right child, L if this node is a left child and Root if this
     * node is the root.
     */
    private String getColor(){
        if (isRed) return "Red";
        return "Black";
    }
    
    public T getCompareObj() {
		return compareObj;
	}

	/**
     * Returns string representation of red-black tree.
     *
     * @pre returns string representation of red-black tree
     */
    public String toString()
    {
        if (isEmpty()) return "";
        if (isRed()) return "(" + left() + value() + right() +")";
        else         return "[" + left() + value() + right() +"]";
    }
    
    public static class OperatorResult<T, K extends RedBlackHashTreeComparable<T>, V> {
    	
    	private RedBlackHashTree<T, K, V> tree;
    	
    	private Object retResult;

		public OperatorResult(RedBlackHashTree<T, K, V> root, Object retResult) {
			this.tree = root;
			this.retResult = retResult;
		}

		public RedBlackHashTree<T, K, V> getTree() {
			return tree;
		}

		public Object getValue() {
			return retResult;
		}
    	
    }
}