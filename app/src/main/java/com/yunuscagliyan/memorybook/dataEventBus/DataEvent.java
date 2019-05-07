package com.yunuscagliyan.memorybook.dataEventBus;

public class DataEvent {
    public static class ShowAddNoteDialog{
        private int trigger;

        public ShowAddNoteDialog(int trigger) {
            this.trigger = trigger;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }
    }
    public static class UpdateDataTrigger{
        private int trigger;

        public UpdateDataTrigger(int trigger) {
            this.trigger = trigger;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }
    }
    public static class SwipedNotePosition{
        private int position;

        public SwipedNotePosition(int trigger) {
            this.position = trigger;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
    public static class DialogNotePositionComplete{
        private int position;

        public DialogNotePositionComplete(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
    public static class DialogPositionComplete{
        private int position;

        public DialogPositionComplete(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
    public static class CompleteNotePosition{
        private int position;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public CompleteNotePosition(int position) {
            this.position = position;
            isChecked=false;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}
