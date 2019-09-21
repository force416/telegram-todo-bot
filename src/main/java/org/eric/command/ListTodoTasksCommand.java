package org.eric.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.eric.model.Task;
import org.eric.service.TaskService;
import org.eric.utils.SpringContext;

import java.util.List;

public class ListTodoTasksCommand extends Command {
    @Override
    public void run(Update update) {
        long chatId = update.message().chat().id();

        TaskService taskService = SpringContext.getBean(TaskService.class);
        List<Task> tasks = taskService.getTodoTasks(chatId);

        String rep = tasks.stream()
                .map((task) -> {
                    String hash = task.getHash();
                    String content = task.getContent();
                    String status = task.getStatus();
                    return String.format("[ %s ] %s  \r\n", hash, content);
                })
                .reduce("", (str, content) -> {
                    return str + content;
                });

        this.sendMsg(new SendMessage(chatId, rep)
                .disableWebPagePreview(true));
    }
}