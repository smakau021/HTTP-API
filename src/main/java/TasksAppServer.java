
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.plugin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.Objects;

/**
 *
 *
 * Application Server for the Tasks API
 */
public class TasksAppServer {
    private static final TasksDatabase database = new TasksDatabase();


    private final Javalin appServer;


    /**
     * Create the application server and configure it.
     */
    public TasksAppServer() {
        this.appServer = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.jsonMapper(createGsonMapper());


        });

        this.appServer.get("/tasks", this::getAllTasks);
        this.appServer.get("/task/{id}", TasksAppServer::getOneTask);
        this.appServer.post("task", TasksAppServer::create);
    }

    /**
     * Use GSON for serialisation instead of Jackson
     * because GSON allows for serialisation of objects without noargs constructors.
     *
     * @return A JsonMapper for Javalin
     */
    private static JsonMapper createGsonMapper() {
        Gson gson = new GsonBuilder().create();
        return new JsonMapper() {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj) {
                return gson.toJson(obj);
            }

            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Class<T> targetClass) {
                return gson.fromJson(json, targetClass);
            }
        };
    }

    /**
     * Start the application server
     *
     * @param port the port for the app server
     */
    public void start(int port) {
        this.appServer.start(port);
    }

    /**
     * Stop the application server
     */
    public void stop() {
        this.appServer.stop();
    }

    /**
     * Get all tasks
     *
     * @param context the server context
     */
    private void getAllTasks(Context context) {
        context.contentType("application/json");
        context.json(database.all());
    }

    private static void getOneTask(Context context) {
        context.contentType("application/json");
        Integer id = context.pathParamAsClass("id", Integer.class).get();
        context.json(id);

        try {
            context.json(database.get(id));
        } catch (NullPointerException e) {
            context.status(404);
        }
    }

    public static void create(Context context) {
        Task task = context.bodyAsClass(Task.class);
        boolean newTask = database.add(task);
        if (newTask) {
            context.header("Location", "/task/" + task.getId());
            context.status(HttpCode.CREATED);
            context.json(task);
        } else throw new BadRequestResponse();



    }
}
