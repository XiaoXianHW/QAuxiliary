/*
 * QAuxiliary - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2022 qwq233@qwq2333.top
 * https://github.com/cinit/QAuxiliary
 *
 * This software is non-free but opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by QAuxiliary contributors.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/cinit/QAuxiliary/blob/master/LICENSE.md>.
 */

package io.github.qauxv.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.os.Process
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import cc.ioctl.util.LayoutHelper
import io.github.qauxv.BuildConfig
import io.github.qauxv.R
import io.github.qauxv.util.Log
import io.github.qauxv.util.Toasts
import io.github.qauxv.util.hostInfo
import xyz.nextalone.util.SystemServiceUtils

class DebugLogFragment : BaseRootLayoutFragment() {

    private var mTextView: TextView? = null
    private var mScrollView: ScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        title = "模块调试日志"
    }

    override fun doOnCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = inflater.context
        val textView = TextView(ctx).apply {
            textSize = 12f
            typeface = Typeface.MONOSPACE
            setTextIsSelectable(true)
            setTextColor(ResourcesCompat.getColor(resources, R.color.firstTextColor, ctx.theme))
            val dp8 = LayoutHelper.dip2px(ctx, 8f)
            setPadding(dp8, 0, dp8, dp8)
        }
        mTextView = textView
        val scrollView = ScrollView(ctx).apply {
            addView(textView, ViewGroup.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))
            rootLayoutView = this
        }
        mScrollView = scrollView
        reloadLogs()
        return scrollView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mTextView = null
        mScrollView = null
    }

    private fun buildLogText(): String = buildString {
        append(BuildConfig.VERSION_NAME).append('\n')
        append(hostInfo.hostName).append(hostInfo.versionName)
        append('(').append(hostInfo.versionCode).append(')').append('\n')
        append("PID: ").append(Process.myPid()).append(", UID: ").append(Process.myUid()).append('\n')
        append('\n')
        val logs = Log.getBufferedLogs()
        append(logs.ifEmpty { "(暂无日志)\n" })
    }

    private fun reloadLogs() {
        val textView = mTextView ?: return
        textView.text = buildLogText()
        mScrollView?.post { mScrollView?.fullScroll(View.FOCUS_DOWN) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.debug_log_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_debug_log_refresh -> {
                reloadLogs()
                true
            }

            R.id.menu_item_debug_log_copy -> {
                val ctx = requireContext()
                SystemServiceUtils.copyToClipboard(ctx, buildLogText())
                Toasts.show(ctx, "已复制到剪贴板")
                true
            }

            R.id.menu_item_debug_log_clear -> {
                Log.clearBufferedLogs()
                reloadLogs()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
