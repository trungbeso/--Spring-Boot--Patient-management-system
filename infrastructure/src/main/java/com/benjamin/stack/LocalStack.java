package com.benjamin.stack;

import software.amazon.awscdk.*;

public class LocalStack extends Stack{

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope, id, props);
    }

    /* entry point to create cloud formation template for infrastructure*/
    public static void main(final String[] args) {
        App app = new App(AppProps.builder().outdir("./cdk.out").build());

        /* define some additional properties you want to apply to ur stack */
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer()) // Synthesizer is an ABS term used to convert code into a cloud formation template
                .build();

        new LocalStack(app, "localstack", props);
        app.synth();
        /* app.synth tell cdk that we want to take our stack + any props add to ./cdk.out folder */
        System.out.println("App synthesizing in progress . . .");
    }
}
