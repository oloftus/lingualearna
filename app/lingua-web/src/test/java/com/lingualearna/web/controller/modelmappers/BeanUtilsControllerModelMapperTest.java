package com.lingualearna.web.controller.modelmappers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.BeanUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BeanUtils.class, BeanUtilsControllerModelMapper.class })
public class BeanUtilsControllerModelMapperTest {

    BeanUtilsControllerModelMapper<Object, Object> mapper;
    @Mock
    private Object model;
    @Mock
    private Object entity;
    String ignore;

    @Before
    public void setup() {

        PowerMockito.mockStatic(BeanUtils.class);
        mapper = new BeanUtilsControllerModelMapper<Object, Object>();
    }

    @Test
    public void testCopyFromModel() {

        whenICallFromModel();
        thenTheModelPropertiesAreCopiedRespectingIgnores();
    }

    @Test
    public void testCopyFromEntity() {

        whenICallFromEntity();
        thenTheEntityPropertiesAreCopiedRespectingIgnores();
    }

    private void whenICallFromEntity() {

        mapper.fromEntity(entity, model, ignore);
    }

    private void thenTheEntityPropertiesAreCopiedRespectingIgnores() {

        PowerMockito.verifyStatic();
        BeanUtils.copyProperties(entity, model, ignore);
    }

    private void thenTheModelPropertiesAreCopiedRespectingIgnores() {

        PowerMockito.verifyStatic();
        BeanUtils.copyProperties(model, entity, ignore);
    }

    private void whenICallFromModel() {

        mapper.fromModel(model, entity, ignore);
    }
}
