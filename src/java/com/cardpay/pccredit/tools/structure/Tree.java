package com.cardpay.pccredit.tools.structure;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/17.
 * 简单树
 */
public class Tree<T> {
    /**
     * 创建树类型节点
     */
    private class Node<T>{
        /**
         * 父节点
         */
        private Node<T> parent;
        /**
         * 子节点列表
         */
        private ArrayList<Node<T>> childList;
        /**
         * 子节点数据
         */
        private T data;
        /**
         * 是否叶子节点
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
     * 回溯寻找内容匹配的节点
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
     * 新增叶子节点 todo:设置父亲节点，树合并
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
