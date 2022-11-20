package com.kartollika.quizzer.domain.datasource

import com.kartollika.quizzer.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
  fun locationEnabled(): Boolean
  fun getLocations(): Flow<Location>
  fun getLastKnownLocation(): Flow<Location?>
}