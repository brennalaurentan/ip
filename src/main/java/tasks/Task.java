package tasks;
public abstract class Task {
    protected String description;

    protected String taskCode;
    protected boolean isDone;

    public Task() {
        this.description = "NO DESCRIPTION";
        this.isDone = false;
    }

    public Task(String description, String taskCode) {
        this.description = description;
        this.taskCode = taskCode;
        this.isDone = false;
    }

    public Task(String description, String taskCode, boolean isDone) {
        this.description = description;
        this.taskCode = taskCode;
        this.isDone = isDone;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public String getTaskCode() { return this.taskCode; }

    public boolean isDone() {
        return this.isDone;
    }

    public abstract String getTaskDetails();

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public abstract Task convertSaveToTask(String saveDetails);

    public abstract String convertTaskToSave();
}