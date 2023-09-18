package com.lexxkit.stmmicroservices.ticketpurchase.mapper;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.TicketDto;
import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

  TicketDto toDto(Ticket ticket);

  List<TicketDto> toDtoList(List<Ticket> tickets);

  Ticket toModel(TicketDto ticketDto);
}
