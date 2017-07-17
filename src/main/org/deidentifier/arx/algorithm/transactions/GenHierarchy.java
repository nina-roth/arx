package org.deidentifier.arx.algorithm.transactions;


import java.util.Arrays;

public class GenHierarchy {

    private int[][] hierarchy;
    private int[] domainItems;
    private int[] leafsUnderItem;


    public GenHierarchy(String[][] h, Dict d) {
        hierarchy = new int[h.length][h[0].length];
        domainItems = new int[h.length];
        int size = 0;
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[0].length; j++) {
                hierarchy[i][j] = d.getRepresentation(h[i][j]);
                size++;
            }
            this.domainItems[i] = d.getRepresentation(h[i][0]);
        }

        leafsUnderItem = new int[size];
        int leafCount = 0;
        for (int k = domainItems.length; k < size; k++) {
            for (int i = 0; i < domainItems.length; i++) {
                int[] a =  toRoot(i);
                for (int anA : a) {
                    if (anA == k) {
                        leafCount++;
                        break;
                    }
                }
            }
            leafsUnderItem[k] = leafCount;
            leafCount = 0;
        }


    }

    public GenHierarchy(int[][] hierarchy) {
        this.hierarchy = hierarchy;
        int size = 0;
        for (int[] aHierarchy : hierarchy) {
            size += aHierarchy.length;
        }

        leafsUnderItem = new int[size];
        int leafCount = 0;
        for (int k = domainItems.length; k < size; k++) {
            for (int i = 0; i < domainItems.length; i++) {
                int[] a =  toRoot(i);
                for (int anA : a) {
                    if (anA == k) {
                        leafCount++;
                        break;
                    }
                }
                leafsUnderItem[k] = leafCount;
                leafCount = 0;
            }
        }

    }
    public int getLeafsUnderGeneralization(int i){
        return leafsUnderItem[i];
    }

    /**
     * @param item the item for which the generalizations should be returned
     * @return All generalizations of item, inclusively the item itself at index 0
     */
    public int[] toRoot(int item) {
        if (item < this.hierarchy.length)
            return this.hierarchy[item];
        else {
            for (int[] path : this.hierarchy) {
                for (int j = 0; j < path.length; j++) {
                    if (path[j] == item)
                        return Arrays.copyOfRange(path, j, path.length);
                }
            }
        }
        return null;
    }


    /**
     * @param item           an item
     * @param generalization an item
     * @return true, if generalization is a generalization of item
     */
    public boolean generalizes(int item, int generalization) {
        int[] pathToRoot = toRoot(item);
        for (int i : pathToRoot) {
            if (i == generalization)
                return true;
        }
        return false;
    }

    public int[] getDomainItems() {
        return domainItems;
    }

    public int[][] getHierarchy() {
        return hierarchy;
    }

    // solely for debugging purposes. will be deleted
    public String rep(Dict d) {
        StringBuilder s = new StringBuilder();
        s.append("strict digraph gtree {\n");
        for (int[] aHierarchy : hierarchy) {
            for (int j = 0; j < aHierarchy.length - 1; j++) {
                s.append('"')
                        .append(d.getString(aHierarchy[j + 1]))
                        .append('"')
                        .append("->")
                        .append('"')
                        .append(d.getString(aHierarchy[j]))
                        .append('"')
                        .append("\n");
            }
            s.append("\n");
        }
        s.append("}");
        return s.toString();
    }
}
