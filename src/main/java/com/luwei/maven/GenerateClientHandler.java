package com.luwei.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by lhcpig on 2017/7/20.
 */
@Mojo(name = "gen-handler")
public class GenerateClientHandler extends AbstractMojo {
    @Component
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("-----------------------start create handler---------------------------------");
        String handlerDir = project.getBuild().getSourceDirectory() + Utils.pack;
        String template = Utils.getTemplate("/Handler.java");
        String clientProto = Utils.getClientProto(project);
        new HandlerGenerator(handlerDir, template, clientProto).build();
        new GenerateClientCommands(project).execute();
    }


}
