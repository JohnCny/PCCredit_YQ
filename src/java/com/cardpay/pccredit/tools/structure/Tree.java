package com.cardpay.pccredit.tools.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 2017/7/17.
 * ����
 */
public class Tree<T> {
    /**
     * ���������ͽڵ�
     */
    private class Node<T>{
        /**
         * ���ڵ�
         */
        private Node<T> parent;
        /**
         * �ӽڵ��б�
         */
        private ArrayList<Node<T>> childList;
        /**
         * �ӽڵ�����
         */
        private T data;
        /**
         * �Ƿ�Ҷ�ӽڵ�
         */
        private boolean isLeaf;

        public Node(T data){
            setData(data);
            setIsLeaf(true);
            ArrayList<Node<T>> childList=new ArrayList<Node<T>>();
            setChildList(childList);
        }

        public void addNode(Node<T> node){
            setIsLeaf(false);
            node.setParent(this);
            if (node.getChildList().size()>0) {
                ArrayList<Node<T>> childList = getChildList();
                childList.addAll(node.getChildList());
            }
            else
                getChildList().add(node);
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public ArrayList<Node<T>> getChildList() {
            return childList;
        }

        public void setChildList(ArrayList<Node<T>> childList) {
            this.childList = childList;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setIsLeaf(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }
    }

    private Node root;

    private static int size;

    private List<T> dataList;

    public Tree(){
        Node root=new Node("root");
        size=1;
        setRoot(root);
    }

    /**
     * ����Ѱ������ƥ��Ľڵ�
     * @param currentNode
     * @param data
     * @return
     */
    public Node findNode(Node currentNode,T data){
        Node resultNode=currentNode;
        if (resultNode.data!=data) {
            if (!currentNode.isLeaf()) {
                for (int i = 0; i < currentNode.getChildList().size(); i++)
                    resultNode = findNode((Node) currentNode.getChildList().get(i), data);
            }
        }
        return resultNode;
    }

    /**
     * �����ڵ�
     * @param parentData
     * @param data
     */
    public void addNode(T parentData,T data){
        Node root=getRoot();

        Node findParentNode=findNode(root, parentData);
        Node findSonNode=findNode(root,data);

        if(findParentNode.getData()=="root" && findSonNode.getData()=="root"){
            Node parentNode=new Node(parentData);
            Node sonNode = new Node(data);
            parentNode.addNode(sonNode);
            root.addNode(parentNode);
            size+=2;
        }
        else if(findParentNode.getData()!="root" && findSonNode.getData()=="root"){
            Node sonNode=new Node(data);
            findParentNode.addNode(sonNode);
            size+=1;
        }

    }

    /**
     * ������
     * @param node
     */
    public void traversalTree(Node node){
        if(node.isLeaf) {
            dataList.add((T) node.getData());
        }
        for (int i=0;i<node.getChildList().size();i++) {
            traversalTree((Node)node.getChildList().get(i));
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
