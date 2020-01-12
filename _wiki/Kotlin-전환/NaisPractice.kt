// recyclerview에 직접 데이터 뿌려주는 것 처리 --> CommodityFragment에서 bundle로 parameter값들을 받아와서 처리(db에서 먼저 확인)
        mCommodityViewModel!!.getCommodities(mMode!!, mMarketId!!, date, searchText)
                .observe(parentFragment!!, { commodities ->
                    if (isCloseFragment) {
                        return@mCommodityViewModel
                    }

                    mExpandableAdapter!!.removeAllSections()

                    setVisibility(mBinding!!.rvCommodities, View.GONE)
                    setVisibility(mBinding!!.rlNoData.rlNoData, View.VISIBLE)
                    setVisibility(mBinding!!.rlNoData.ivNoData, View.GONE)
                    setVisibility(mBinding!!.rlNoData.pbLoading, View.VISIBLE)

                    if (commodities == null || commodities.isEmpty()) {
                        mExpandableAdapter!!.notifyDataSetChanged()

                        setVisibility(mBinding!!.rlNoData.ivNoData, View.VISIBLE)
                        setVisibility(mBinding!!.rlNoData.pbLoading, View.GONE)

                        return@mCommodityViewModel
                    }

                    val commoditiesMap = LoadCommodityWithCategory().execute(requireContext(), commodities)
                    for ((key, value) in commoditiesMap) {
                        if (value.size > 0) {
                            mExpandableAdapter!!.addSection(key,
                                    ExpandableCommoditySection(
                                            key,
                                            value,
                                            this,
                                            this))
                        }
                    }

                    setVisibility(mBinding!!.rlNoData.rlNoData, View.GONE)
                    setVisibility(mBinding!!.rvCommodities, View.VISIBLE)
                })
