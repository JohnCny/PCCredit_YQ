package com.cardpay.pccredit.tools.structure;

import java.util.ArrayList;

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
            ArrayList<Node<T>> childList=getChildList();
            childList.addAll(node.getChildList());

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

    public Tree(T data){
        Node root=new Node(data);
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
            while (!currentNode.isLeaf()) {
                for (int i = 0; i < currentNode.getChildList().size(); i++)
                    resultNode = findNode((Node) currentNode.getChildList().get(i), data);
            }
        }
        return resultNode;
    }

    /**
     * ����Ҷ�ӽڵ� todo:���ø��׽ڵ㣬���ϲ�
     * @param parentData
     * @param data
     */
    public void addData(T parentData,T data){
        Node root=getRoot();
        Node parentNode=findNode(root,parentData);
        Node currentNode=new Node(data);

        parentNode.addNode(currentNode);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
