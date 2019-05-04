package com.yunuscagliyan.memorybook.data;

public class Notes {
    private int id;
    private String noteContent;
    private String noteDate;
    private int noteComplete;

    public Notes(String noteContent) {
        this.noteContent = noteContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public int getNoteComplete() {
        return noteComplete;
    }

    public void setNoteComplete(int noteComplete) {
        this.noteComplete = noteComplete;
    }
}
