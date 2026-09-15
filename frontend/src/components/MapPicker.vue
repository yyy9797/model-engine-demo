<template>
  <el-form-item :label="field.name">
    <div ref="mapContainer" style="height:300px"></div>
    <div style="margin-top:8px">
      <el-button type="primary" @click="emitUpdate">保存坐标</el-button>
      <span style="margin-left:12px">{{ coordsText }}</span>
    </div>
  </el-form-item>
</template>

<script>
import { ref, onMounted } from 'vue'
import L from 'leaflet'
export default {
  props: ['field','value'],
  setup(props, { emit }) {
    const mapContainer = ref(null)
    let map, marker
    const coordsText = ref('')
    onMounted(() => {
      map = L.map(mapContainer.value).setView([31.2304, 121.4737], 10)
      L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map)
      map.on('click', (e) => {
        if (marker) marker.setLatLng(e.latlng)
        else marker = L.marker(e.latlng).addTo(map)
        coordsText.value = `${e.latlng.lat.toFixed(6)}, ${e.latlng.lng.toFixed(6)}`
      })
      if (props.value && props.value.coordinates) {
        const [lng, lat] = props.value.coordinates
        marker = L.marker([lat, lng]).addTo(map)
        map.setView([lat, lng], 12)
        coordsText.value = `${lat.toFixed(6)}, ${lng.toFixed(6)}`
      }
    })
    function emitUpdate() {
      if (marker) {
        const latlng = marker.getLatLng()
        emit('update', { type: 'Point', coordinates: [latlng.lng, latlng.lat] })
      } else alert('请先在地图上点击选择位置')
    }
    return { mapContainer, emitUpdate, coordsText }
  }
}
</script>
