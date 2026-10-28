package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MetricKpiCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier,
  badge: String? = null,
  onClick: (() -> Unit)? = null
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
      .testTag("kpi_card_${title.replace(" ", "_")}"),
    colors = CardDefaults.cardColors(
      containerColor = MaterialNavyCard
    ),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.4f), Color.Transparent))
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(accentColor.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(24.dp)
          )
        }
        if (badge != null) {
          Surface(
            color = accentColor.copy(alpha = 0.2f),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
          ) {
            Text(
              text = badge,
              color = accentColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )

      Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Slate400,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = accentColor,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun StatusBadge(
  text: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(8.dp),
    color = color.copy(alpha = 0.18f),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.45f))
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(color)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = color
      )
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  actionText: String? = null,
  onActionClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          fontSize = 12.sp,
          color = Slate400
        )
      }
    }
    if (actionText != null && onActionClick != null) {
      TextButton(
        onClick = onActionClick,
        modifier = Modifier.testTag("action_btn_${actionText.replace(" ", "_")}")
      ) {
        Text(
          text = actionText,
          color = MedicalCyan,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp
        )
      }
    }
  }
}
