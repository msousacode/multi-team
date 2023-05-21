package com.multiteam.modules.schedule;

import com.multiteam.core.enums.ScheduleEnum;
import com.multiteam.core.models.Tenantable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.GregorianCalendar;
import java.util.UUID;

@Entity
@Table(name = "schedules")
public class Schedule extends Tenantable {

        @Id
        @GeneratedValue
        @Column(name = "schedule_id")
        private UUID id;

        @Column(name = "title")
        private String title;

        @Column(name = "start")
        private GregorianCalendar start;

        @Column(name = "end")
        private GregorianCalendar end;

        @Column(name = "url")
        private String url;

        @Column(name = "description")
        private String description;

        @Column(name = "color")
        private String color;

        @Column(name = "active")
        private boolean active;

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        private ScheduleEnum status;
}
