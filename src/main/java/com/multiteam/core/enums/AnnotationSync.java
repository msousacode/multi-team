package com.multiteam.core.enums;

import lombok.Getter;

public enum AnnotationSync {
    INCLUIR(0),
    INATIVAR(2),
    ATUALIZAR(4);

    AnnotationSync(int sync) {
        this.sync = sync;
    }

    @Getter
    private int sync;
}
