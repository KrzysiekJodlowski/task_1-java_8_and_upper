import com.epam.jmp.impl.service.ServiceImpl;

module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;

    provides com.epam.jmp.service.Service with ServiceImpl;
    exports com.epam.jmp.impl.service;
}
