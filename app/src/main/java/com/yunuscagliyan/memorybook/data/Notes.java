package com.yunuscagliyan.memorybook.data;

//Data Model
public class Notes {
    private int id;
    private String noteContent;
    private long noteDate;
    private int noteComplete;
    private long noteInsertionDate;

    public long getNoteInsertionDate() {
        return noteInsertionDate;
    }

    public void setNoteInsertionDate(long noteInsertionDate) {
        this.noteInsertionDate = noteInsertionDate;
    }

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

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public int getNoteComplete() {
        return noteComplete;
    }

    public void setNoteComplete(int noteComplete) {
        this.noteComplete = noteComplete;
    }
}
