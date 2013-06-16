/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.UUID;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;

@ManagedBean("mindmapController")
@SessionScoped
public class MindmapController implements Serializable {

    private MindmapNode root;
    private MindmapNode selectedNode;

    public MindmapController() {
        root = new DefaultMindmapNode("Inicio", "Inicio", "FFCC00", false);

        MindmapNode fim = new DefaultMindmapNode("Fim", "Fim", "FFCC00", false);
        
        
        root.addNode(fim);
        
    }

    public MindmapNode getRoot() {
        return root;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect(SelectEvent event) {
        MindmapNode node = (MindmapNode) event.getObject();

        //populate if not already loaded  
        if (node.getChildren().isEmpty()) {
            Object label = node.getLabel();

            if (label.equals("NS(s)")) {
                for (int i = 0; i < 25; i++) {
                    node.addNode(new DefaultMindmapNode("ns" + i + ".google.com", "Namespace " + i + " of Google", "82c542"));
                }
            } else if (label.equals("IPs")) {
                for (int i = 0; i < 18; i++) {
                    node.addNode(new DefaultMindmapNode("1.1.1." + i, "IP Number: 1.1.1." + i, "fce24f"));
                }

            } else if (label.equals("Malware")) {
                for (int i = 0; i < 18; i++) {
                    String random = UUID.randomUUID().toString();
                    node.addNode(new DefaultMindmapNode("Malware-" + random, "Malicious Software: " + random, "3399ff", false));
                }
            }
        }

    }

    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();
    }
}
