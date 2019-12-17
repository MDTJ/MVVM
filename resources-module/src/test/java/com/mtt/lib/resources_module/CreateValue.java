package com.mtt.lib.resources_module;

import org.junit.Test;

public class CreateValue {
    @Test
    public void createValue(){
        GenerateValueFiles generateValueFiles = new GenerateValueFiles(750,1334,"");
        generateValueFiles.generate();
    }
}
