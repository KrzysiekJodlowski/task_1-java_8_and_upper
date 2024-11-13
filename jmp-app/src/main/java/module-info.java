module jmp.app {
    requires transitive jmp.bank.api;
    requires transitive jmp.service.api;
    requires jmp.cloud.bank.impl;
    requires jmp.cloud.service.impl;
    requires jmp.dto;
}