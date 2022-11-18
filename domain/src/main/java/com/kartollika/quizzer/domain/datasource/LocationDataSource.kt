package com.kartollika.quizzer.domain.datasource

import com.kartollika.quizzer.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
  fun getLocation(): Flow<Location>
}