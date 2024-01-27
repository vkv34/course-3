package repository

import model.CourseCategoryDto
import repository.defaults.Repository

interface CourseCategoryRepository: Repository<CourseCategoryDto, Int> {

}