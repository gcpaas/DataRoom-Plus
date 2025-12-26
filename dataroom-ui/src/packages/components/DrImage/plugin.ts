import { ChartPlugin } from '@/packages/components/type/define.ts'
import thumbnail from './images/图片.png'

export default class DrImagePlugin extends ChartPlugin {
  constructor(tags: string[]) {
    super('DrImage', '图片', '图片,素材', thumbnail, tags)
  }
}
