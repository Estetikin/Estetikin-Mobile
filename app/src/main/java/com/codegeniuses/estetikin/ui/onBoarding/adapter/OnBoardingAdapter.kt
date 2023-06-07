package com.codegeniuses.estetikin.ui.onBoarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ActivityOnBoardingBinding
import com.codegeniuses.estetikin.ui.onBoarding.OnBoardingActivity.Companion.MAX_STEP

class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingPager>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingPager {
        return OnBoardingPager(
            ActivityOnBoardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = MAX_STEP

    override fun onBindViewHolder(holder: OnBoardingPager, position: Int) {
        holder.itemView.run {
            with(holder) {
                if (position == 0) {
                    bindingDesign.tvTitle.text = context.getString(R.string.title_onboarding_1)
                    bindingDesign.tvDescription.text =
                        context.getString(R.string.description_onboarding_1)
                    bindingDesign.ivIllustration.setImageResource(R.drawable.illustration_boarding_1)
                }
                if (position == 1) {
                    bindingDesign.tvTitle.text = context.getString(R.string.title_onboarding_2)
                    bindingDesign.tvDescription.text =
                        context.getString(R.string.description_onboarding_2)
                    bindingDesign.ivIllustration.setImageResource(R.drawable.illustration_boarding_2)
                }
            }
        }
    }
}
