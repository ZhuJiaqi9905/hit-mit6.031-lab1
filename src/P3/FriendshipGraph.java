package P3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FriendshipGraph {
    private Map<String, List<Person>> graph;
    public FriendshipGraph(){
        graph = new HashMap<>();
    }
    public void addVertex(Person p){
        Set<String> nameSet = graph.keySet();
        //判断是否有重名
        if(nameSet.contains(p.getName())){
            System.out.println("name repeat : " + p.getName());
            System.exit(0);
        }
        //没重名的话，新建一个对应关系
        List<Person> personList = new ArrayList<>();
        graph.put(p.getName(), personList);
    }
    public void addEdge(Person from, Person to){
        List<Person> list = graph.get(from.getName());
        if(list == null){
            System.out.println("person " + from.getName() + " not in graph");
            System.exit(0);
        }
        if(!graph.containsKey(to.getName())){
            System.out.println("person " + to.getName() + " not in graph");
            System.exit(0);
        }
        list.add(to);
    }
    public int getDistance(Person from, Person to){
        //记录姓名和距离的内部类
        class Node{
            String name;
            int distance;
            public Node(String name, int distance) {
                this.name = name;
                this.distance = distance;
            }
        }
        //队列
        Queue<Node> queue = new ArrayDeque<>();
        //判断是否已经经过的set
        Set<String> visited = new HashSet<>();
        //起点
        Node start = new Node(from.getName(), 0);
        queue.offer(start); visited.add(start.name);
        while(!queue.isEmpty()){
            Node head = queue.poll();
            if(head.name.equals(to.getName())){
                //找到了
                return head.distance;
            }
            List<Person> friends = graph.get(head.name);
            for(Person p: friends){
                if(!visited.contains(p.getName())){
                    Node node = new Node(p.getName(), head.distance + 1);
                    queue.offer(node); visited.add(node.name);
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        /*
        Person wrong = new Person("Ross");
        graph.addVertex(wrong);

         */
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        Person notIn = new Person("zhangSan");
       // graph.addEdge(notIn, rachel);
      //  graph.addEdge(rachel, notIn);
    }
}
