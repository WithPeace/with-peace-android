package com.withpeace.withpeace.feature.home.filtersetting.uistate

import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.feature.home.uistate.PolicyFiltersUiModel

data class FilterListUiState(
    val isClassificationExpanded: Boolean = false,
    val isRegionExpanded: Boolean = false,
) {
    private val allClassifications: List<ClassificationUiModel> = ClassificationUiModel.entries
    private val allRegions: List<RegionUiModel> = RegionUiModel.entries

    fun getStateByFilterState(filtersUiModel: PolicyFiltersUiModel): FilterListUiState {
        return this.copy(
            isClassificationExpanded = allClassifications.indexOf(filtersUiModel.classifications.lastOrNull()) >= FOLDED_CLASSIFICATION_ITEM_COUNT,
            isRegionExpanded =  allRegions.indexOf(filtersUiModel.regions.lastOrNull()) >= FOLDED_REGION_ITEM_COUNT,
        )
    }

    fun getClassifications(): List<ClassificationUiModel> {
        return if (isClassificationExpanded) allClassifications.dropLast(ETC_COUNT)
        else allClassifications.subList(0, FOLDED_CLASSIFICATION_ITEM_COUNT)
    }

    fun getRegions(): List<RegionUiModel> {
        return if (isRegionExpanded) allRegions.dropLast(ETC_COUNT)
        else allRegions.subList(0, FOLDED_REGION_ITEM_COUNT)
    }

    companion object {
        private const val FOLDED_CLASSIFICATION_ITEM_COUNT = 3
        private const val FOLDED_REGION_ITEM_COUNT = 7
        private const val ETC_COUNT = 1
    }
}