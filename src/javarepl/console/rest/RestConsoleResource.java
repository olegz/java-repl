package javarepl.console.rest;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Function1;
import com.googlecode.utterlyidle.MediaType;
import com.googlecode.utterlyidle.annotations.FormParam;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import javarepl.console.Console;
import javarepl.console.ConsoleLog;
import javarepl.console.commands.CommandResult;

import static com.googlecode.funclate.Model.persistent.model;
import static com.googlecode.totallylazy.Sequences.sequence;

public class RestConsoleResource {
    private final Console console;

    public RestConsoleResource(Console console) {
        this.console = console;
    }

    @POST
    @Path("execute")
    @Produces(MediaType.APPLICATION_JSON)
    public Model execute(@FormParam("expression") String expression) {
        return resultToModel(console.execute(expression));
    }

    public static Model resultToModel(CommandResult result) {
        return model()
                .add("expression", result.command())
                .add("logs", sequence(result.logs()).map(commandResultToModel()));
    }

    private static Function1<ConsoleLog, Model> commandResultToModel() {
        return new Function1<ConsoleLog, Model>() {
            public Model call(ConsoleLog consoleLog) throws Exception {
                return model().add("type", consoleLog.type())
                        .add("message", consoleLog.message());
            }
        };
    }

}
