package com.example.doancnpm.Objects;

import java.util.List;

public class ComputerGroup {
    private String groupName;
    private List<Computer> computers;
    private boolean isExpanded;

    public ComputerGroup(String groupName, List<Computer> computers) {
        this.groupName = groupName;
        this.computers = computers;
        this.isExpanded = false;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Computer> getComputers() {
        return computers;
    }

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}