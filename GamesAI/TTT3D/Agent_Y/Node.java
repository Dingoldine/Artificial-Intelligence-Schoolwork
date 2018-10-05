//package ttt3d;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private List<Node<T>> children = new ArrayList<Node<T>>();
    private Node<T> parent = null;
    private T data = null;
    public GameState state = null;
    

    public Node(){

    }
    
    public Node(T data) {
        this.data = data;
    }

    

    public Node(GameState state){
        this.state = state;
    }

    public Node(T data, GameState state) {
        this.data = data;
        this.state = state;
    }

    public Node(GameState state, Node<T> parent){
        this.state = state;
        this.parent = parent;
    }

    public Node(T data, GameState state, Node<T> parent){
        this.data = data;
        this.state = state;
        this.parent = parent;
    }

    public T getData() {
        return this.data = data;
    }

    public GameState getState() {
        return state;
    }

    public Node<T> getParent() {
        return this.parent = parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setData(T data) {
        this.data = data;  
    }

    public void setData(T data, GameState state) {
        this.data = data;
        this.state = state;
    }

    public void addChild(T data, GameState state) {
        Node<T> child = new Node<T>(data, state);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(GameState state) {
        Node<T> child = new Node<T>(state);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Node<T> childNode) {
        childNode.setParent(this);
        this.children.add(childNode);
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean isRoot(){
        return (this.parent == null);
    }
 
    public void setParent(Node<T> parent) {      
        this.parent = parent;
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }
}