package com.luwei.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * Created by lhcpig on 2017/7/20.
 */
@Mojo(name = "gen-commands")
public class GenerateClientCommands extends AbstractMojo {
    @Component
    private MavenProject project;

    public GenerateClientCommands() {
    }

    GenerateClientCommands(MavenProject project) {
        this.project = project;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("-----------------------start create client commands---------------------------------");
        String clientProto = Utils.getClientProto(project);
        Utils.buildCommands("InCommands", Utils.getAllEntities(clientProto),project);

        String serverProto = Utils.getServerProto(project);
        Utils.buildCommands("OutCommands", Utils.getAllEntities(serverProto),project);
    }


}
