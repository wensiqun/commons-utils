package cn.wensiqun.commons.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import cn.wensiqun.commons.structure.RedBlackHashTree.OperatorResult;


public class RedBlackHashTreeMap<K extends RedBlackHashTreeComparable, V> implements Cloneable, java.io.Serializable
{
	
	/**
     * A reference to the root of the tree
     */
    protected RedBlackHashTree<K, V> root;

    /**
     * The number of nodes in the tree
     */
    protected int count;

    /**
     * Constructs a red-black search tree with no data
     * @post Constructs an empty red-black tree
     */
    public RedBlackHashTreeMap()
    {
        root = new RedBlackHashTree<K, V>();
        count = 0;
    }
    
    /**
     * Checks for an empty binary search tree
     *
     * @post Returns true iff the binary search tree is empty
     * @return True iff the tree contains no data
     */
    public boolean isEmpty()
    {
        return root.isEmpty();
    }

    /**
     * Removes all data from the binary search tree
     *
     * @post Removes all elements from binary search tree
     */
    public void clear()
    {
        root = new RedBlackHashTree<K, V>();
        count = 0;
    }

    /**
     * Determines the number of data values within the tree
     *
     * @post Returns the number of elements in binary search tree
     * 
     * @return The number of nodes in the binary search tree
     */
    public int size()
    {
        return count;
    }
    
    /**
     * Add a (possibly duplicate) value to the red-black  tree, and ensure
     * that the resulting tree is a red-black tree.
     * 
     * @post Adds a value to binary search tree
     * @param val A reference to non-null object
     */
    public V put(K key, V value)
    {
        //Assert.pre(value instanceof Comparable,"value must implement Comparable");
    	OperatorResult<K, V> result = root.put(key, value);
    	root = result.getTree();
        count += (Integer)result.getValue();
        return value;
    }
   
    /**
     * Remove an value "equals to" the indicated value.  Only one value
     * is removed, and no guarantee is made concerning which of duplicate
     * values are removed.  Value returned is no longer part of the
     * structure
     *
     * @post Removes one instance of val, if found
     * 
     * @param val Value sought to be removed from tree
     * @return Value to be removed from tree or null if no value removed
     */
    public V remove(K key){
    	OperatorResult<K, V> result = root.remove(key);
    	if(result.getValue() != null) {
    		this.root = result.getTree();
    		count--;
        	return (V)result.getValue();
    	}
    	return null;
    }
    
    public V get(K key) {
    	return root.get(key);
    }
    
    public Map<K, V> getMap(Comparable compareObj) {
        return root.getMap(compareObj);	
    }

    /**
     * Determines if the red-black search tree contains a value
     *
     * @post Returns true iff val is a value found within the tree
     * 
     * @param val The value sought.  Should be non-null
     * @return True iff the tree contains a value "equals to" sought value
     */
    public boolean contains(K key){
        return root.contains(key);
    }

    /**
     * Returns a string representing tree
     *
     * @post Generates a string representation of the AVLST
     * @return String representation of tree
     */
    public String toString(){
        return root.toString();
    }
    
    /**
     * Returns the hashCode of the value stored by this object.
     *
     * @return The hashCode of the value stored by this object.
     */
    public int hashCode(){
        return root.hashCode();
    } 


    //get first
    public RedBlackHashTree<K, V> firstNode() {
        RedBlackHashTree<K, V> p = root;
        if(nodeIsValid(p)) {
            while(nodeIsValid(p.left()))
                p = p.left();
        }
        return p;
    }

    //get last
    
    public RedBlackHashTree<K, V> lastNode() {
        RedBlackHashTree<K, V> p = root;
        if(nodeIsValid(p)) {
            while(nodeIsValid(p.right()))
                p = p.right();
        }
        return p;
    }
    
    //get ceiling 

    public RedBlackHashTree<K, V> ceilingNode(Comparable compareObj) {
        RedBlackHashTree<K, V> p = root;
        while (nodeIsValid(p)) {
            int cmp = compareObj.compareTo(p.getCompareObj());
            if (cmp < 0) {
                if (nodeIsValid(p.left()))
                    p = p.left();
                else
                    return p;
            } else if (cmp > 0) {
                if (nodeIsValid(p.right())) {
                    p = p.right();
                } else {
                    RedBlackHashTree<K, V> parent = p.parent();
                    RedBlackHashTree<K, V> ch = p;
                    while (nodeIsValid(parent) && ch == parent.right()) {
                        ch = parent;
                        parent = parent.parent();
                    }
                    return parent;
                }
            } else
                return p;
        }
        return null;
    }
    
    public RedBlackHashTree<K, V> ceilingNode(K key) {
        RedBlackHashTree<K, V> result = ceilingNode(key.getComparableObject());
        return result == null ? null : result;
    }
    
    public V ceilingValue(K key) {
        RedBlackHashTree<K, V> result = ceilingNode(key.getComparableObject());
        return result == null ? null : result.value().get(key);
    }
    
    // get Higher
    public RedBlackHashTree<K, V> higherNode(Comparable compareObj) {
        RedBlackHashTree<K, V> p = root;
        while (nodeIsValid(p)) {
            int cmp = compareObj.compareTo(p.getCompareObj());
            if (cmp < 0) {
                if (nodeIsValid(p.left()))
                    p = p.left();
                else
                    return p;
            } else {
                if (nodeIsValid(p.right())) {
                    p = p.right();
                } else {
                    RedBlackHashTree<K, V> parent = p.parent();
                    RedBlackHashTree<K, V> ch = p;
                    while (nodeIsValid(parent) && ch == parent.right()) {
                        ch = parent;
                        parent = parent.parent();
                    }
                    return parent;
                }
            }
        }
        return null;
    }
    
    public RedBlackHashTree<K, V> higherNode(K key) {
        RedBlackHashTree<K, V> result = higherNode(key.getComparableObject());
        return result == null ? null : result;
    }
    
    public V higherValue(K key) {
        RedBlackHashTree<K, V> result = higherNode(key.getComparableObject());
        return result == null ? null : result.value().get(key);
    }
    
    // get floor
    
    public RedBlackHashTree<K, V> floorNode(Comparable compareObj) {
        RedBlackHashTree<K, V> p = root;
        while (nodeIsValid(p)) {
            int cmp = compareObj.compareTo(p.getCompareObj());
            if (cmp > 0) {
                if (nodeIsValid(p.right()))
                    p = p.right();
                else
                    return p;
            } else if (cmp < 0) {
                if (nodeIsValid(p.left())) {
                    p = p.left();
                } else {
                    RedBlackHashTree<K, V> parent = p.parent();
                    RedBlackHashTree<K, V> ch = p;
                    while (nodeIsValid(parent) && ch == parent.left()) {
                        ch = parent;
                        parent = parent.parent();
                    }
                    return parent;
                }
            } else
                return p;

        }
        return null;
    }
    
    public RedBlackHashTree<K, V> floorNode(K key) {
        RedBlackHashTree<K, V> result = floorNode(key.getComparableObject());
        return result == null ? null : result;
    }
    
    public V floorValue(K key) {
        RedBlackHashTree<K, V> result = floorNode(key.getComparableObject());
        return result == null ? null : result.value().get(key);
    }
    
    // get lower
    
    public RedBlackHashTree<K, V> lowerNode(Comparable compareObj) {
        RedBlackHashTree<K, V> p = root;
        while (nodeIsValid(p)) {
            int cmp = compareObj.compareTo(p.getCompareObj());
            if (cmp > 0) {
                if (nodeIsValid(p.right()))
                    p = p.right();
                else
                    return p;
            } else {
                if (nodeIsValid(p.left())) {
                    p = p.left();
                } else {
                    RedBlackHashTree<K, V> parent = p.parent();
                    RedBlackHashTree<K, V> ch = p;
                    while (nodeIsValid(parent) && ch == parent.left()) {
                        ch = parent;
                        parent = parent.parent();
                    }
                    return parent;
                }
            }
        }
        return null;
    }
    
    public RedBlackHashTree<K, V> lowerNode(K key) {
        RedBlackHashTree<K, V> result = lowerNode(key.getComparableObject());
        return result == null ? null : result;
    }
    
    public V lowerValue(K key) {
        RedBlackHashTree<K, V> result = lowerNode(key.getComparableObject());
        return result == null ? null : result.value().get(key);
    }
    
    public RedBlackHashTree<K, V> pollFirstNode() {
        RedBlackHashTree<K, V> rbht = firstNode();
        removeNode(rbht);
        return rbht;
    }

    public RedBlackHashTree<K, V> pollLastNode() {
        RedBlackHashTree<K, V> rbht = lastNode();
        removeNode(rbht);
        return rbht;
    }

    public Iterator<RedBlackHashTree<K, V>> subIterator(Comparable fromCompareObj, boolean fromInclusive, Comparable toCompareObj,
            boolean toInclusive) {
        return new SubIterator(fromCompareObj, toCompareObj, fromInclusive, toInclusive);
    }

    public Iterator<RedBlackHashTree<K, V>> subIterator(Comparable fromCompareObj, Comparable toCompareObj) {
        return subIterator(fromCompareObj, true, toCompareObj, false);
    }

    public Iterator<RedBlackHashTree<K, V>> headIterator(Comparable toCompareObj, boolean inclusive) {
        return subIterator(firstNode().getCompareObj(), true, toCompareObj, inclusive);
    }

    public Iterator<RedBlackHashTree<K, V>> headIterator(Comparable toCompareObj) {
        return subIterator(firstNode().getCompareObj(), true, toCompareObj, false);
    }

    public Iterator<RedBlackHashTree<K, V>> tailIterator(Comparable fromCompareObj, boolean inclusive) {
        return subIterator(fromCompareObj, inclusive, lastNode().getCompareObj(), true);
    }

    public Iterator<RedBlackHashTree<K, V>> tailIterator(Comparable fromCompareObj) {
        return subIterator(fromCompareObj, false, lastNode().getCompareObj(), true);
    }
    
    private void removeNode(RedBlackHashTree<K, V> target) {
    	this.count -= target.value().size();
    	
    	// determine the node to be disconnected:
        // two cases: if degree < 2 we remove target node;
        //            otherwise, remove predecessor
        RedBlackHashTree<K, V> freeNode;
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
        target.compareObj = freeNode.compareObj; // move value compareObj

        // child will be orphaned by the freeing of freeNode;
        // reparent this child carefully (it may be EMPTY)
        RedBlackHashTree<K, V> child;
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
        RedBlackHashTree<K, V> result = child.root();  
        
        if (freeNode.isBlack()) 
            child.blackFixup();
        
        this.root = result.root();
    }
    
    private RedBlackHashTree<K, V> successor(RedBlackHashTree<K, V> t) {
    	
    	if(!nodeIsValid(t)) {
    	    return null;
    	} else if (nodeIsValid(t.right())) {
    		RedBlackHashTree<K, V> p = t.right();
    		while(nodeIsValid(p.left())) {
    			p = p.left();
    		}
    		return p;
    	} else {
    		RedBlackHashTree<K, V> p = t.parent();
    		while(nodeIsValid(p) && t.isRightChild()) {
    			t = p;
    			p = t.parent();
    		}
    		
    		return p;
    	}
    }
    
    private RedBlackHashTree<K, V> predecessor(RedBlackHashTree<K, V> t) {
    	if(!nodeIsValid(t)) {
    	    return null;
    	} else if (nodeIsValid(t.left())) {
    		RedBlackHashTree<K, V> p = t.left();
    		while(nodeIsValid(p.right())) {
    			p = p.right();
    		}
    		return p;
    	} else {
    		RedBlackHashTree<K, V> p = t.parent();
    		while(nodeIsValid(p) && t.isLeftChild()) {
    			t = p;
    			p = t.parent();
    		}
    		
    		return p;
    	}
    }
    
    private boolean nodeIsValid(RedBlackHashTree<K, V> t) {
    	return t != null && !t.isEmpty();
    }
    
    private class SubIterator implements Iterator<RedBlackHashTree<K, V>> {

        private RedBlackHashTree<K, V> current;
        
        private RedBlackHashTree<K, V> next;
        
        private Comparable start;
        
        private Comparable end;
        
        private boolean isReverse;
        
        private boolean includeStart;
        
        private boolean includeEnd;
        
        public SubIterator(Comparable start, Comparable end, boolean includeStart, boolean includeEnd) {
            this.start = start;
        	this.end = end;
            this.includeStart = includeStart;
            this.includeEnd = includeEnd;
            isReverse = ((Comparable)start).compareTo(end) > 0 ? true : false;
        }

        public boolean hasNext() {
            return next != null || (next = traversal()) != null;
        }

        public RedBlackHashTree<K, V> next() {
            if (next != null) {
        		current = next;
        		next = traversal();
        		return current;
        	} 
        	throw new NoSuchElementException();
        }
        
        private RedBlackHashTree<K, V> traversal() {
        	RedBlackHashTree<K, V> node;
        	if(current == null && next == null) {
        		if(includeStart) {
                	if (isReverse) {
                		node = floorNode(start);
                	} else {
                		node = ceilingNode(start);
                	}
                } else {
                    if (isReverse) {
                    	node = lowerNode(start);
                	} else {
                		node = higherNode(start);
                	}
                }
        	} else {
        		node = isReverse ? predecessor(current) : successor(current);
        	}
        	
            if(nodeIsValid(node)) {
            	int comp = ((Comparable)node.getCompareObj()).compareTo(end);
            	if(includeEnd) {
                	if (isReverse) {
                		if (comp >= 0) {
                			return node;
                		}
                	} else {
                		if (comp <= 0) {
                			return node;
                		}
                	}
                } else {
                    if (isReverse) {
                    	if (comp > 0) {
                			return node;
                		}
                	} else {
                    	if (comp < 0) {
                			return node;
                		}
                	}
                }
            }
        	return null;
        }

        public void remove() {
            removeNode(current);
        }
    }
    
}