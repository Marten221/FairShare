package com.example.fairshare.data.mappers

import com.example.fairshare.data.local.entity.GroupEntity
import com.example.fairshare.domain.model.Group

fun GroupEntity.toDomain() = Group(id = id, name = name, memberCount = memberCount)
fun Group.toEntity() = GroupEntity(id = id, name = name, memberCount = memberCount)