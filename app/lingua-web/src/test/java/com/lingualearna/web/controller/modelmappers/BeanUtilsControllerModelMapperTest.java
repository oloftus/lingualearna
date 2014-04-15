package com.lingualearna.web.controller.modelmappers;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.BeanUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BeanUtilsControllerModelMapper.class })
public class BeanUtilsControllerModelMapperTest {

    @Mock
    private Object model;

    @Mock
    private Object entity;

    String ignore;

    BeanUtilsControllerModelMapper<Object, Object> mapper;

    @Before
    public void setup() {

        mockStatic(BeanUtils.class);
        mapper = new BeanUtilsControllerModelMapper<Object, Object>();
    }

    @Test
    public void testCopyFromEntity() {

        whenICallFromEntity();
        thenTheEntityPropertiesAreCopiedRespectingIgnores();
    }

    @Test
    public void testCopyFromModel() {

        whenICallFromModel();
        thenTheModelPropertiesAreCopiedRespectingIgnores();
    }

    private void thenTheEntityPropertiesAreCopiedRespectingIgnores() {

        verifyStatic();
        BeanUtils.copyProperties(entity, model, ignore);
    }

    private void thenTheModelPropertiesAreCopiedRespectingIgnores() {

        verifyStatic();
        BeanUtils.copyProperties(model, entity, ignore);
    }

    private void whenICallFromEntity() {

        mapper.fromEntity(entity, model, ignore);
    }

    private void whenICallFromModel() {

        mapper.fromModel(model, entity, ignore);
    }
}
